package com.izaphe.blemanager.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_graph.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView


import android.widget.AdapterView.OnItemClickListener
import com.androidplot.ui.SizeMetric
import com.androidplot.ui.SizeMode
import com.androidplot.util.Redrawer
import com.androidplot.xy.*
import com.i7xaphe.blemanager.R

import com.izaphe.ble.utils.BlePackedConverter.interpretAsFloat
import com.izaphe.ble.utils.BlePackedConverter.interpretAsInteger
import com.izaphe.ble.utils.BlePackedConverter.interpretAsString

import com.izaphe.blemanager.ble.BleChrateristicInfo
import com.izaphe.blemanager.ble.MultiDeviceCharCollection
import com.izaphe.blemanager.ble.MultiDeviceCharCollection.multiDeviceCharCollection
import com.izaphe.blemanager.ble.MultiDeviceCharCollection.setInterface
import com.izaphe.blemanager.dialogs.DialogCharacteristicSettings
import com.izaphe.blemanager.dialogs.DialogGraphSettings
import com.izaphe.blemanager.dialogs.DialogStandardCharateristicsSettings
import com.izaphe.blemanager.myinterfaces.DialogCharateristicSettingsInterface
import com.izaphe.blemanager.myinterfaces.DialogGraphSettingsInterface
import com.izaphe.blemanager.myinterfaces.MultiDeviceCharCollectionInterface
import java.lang.ClassCastException
import java.sql.Time


import java.text.DecimalFormat
import java.util.*


/**
 * Created by Kamil on 2018-03-17.
 */
class GraphActivity : AppCompatActivity(), OnItemClickListener {


    val context: Context = this
    var counter = 0
    private val series: HashMap<Pair<Int, Pair<Int, Int>>, SimpleXYSeries> = HashMap()
    private var redrawer: Redrawer? = null
    private var historySize = 100

    fun getRandomColor(): Int {
        val rnd = Random()
        rnd.setSeed(System.currentTimeMillis())
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        registerReceiver(broadcastReceiver, makeIntentFilter())

        listview_selected_char.onItemClickListener = this


        plot.setOnClickListener({
            var dialog = DialogGraphSettings(this, object : DialogGraphSettingsInterface {
                override fun historySize(historySize: Int) {
                    plot!!.setDomainBoundaries(0, historySize, BoundaryMode.FIXED)
                    this@GraphActivity.historySize = historySize
                }
            }, historySize)
            dialog.show()
        })

        setInterface(object : MultiDeviceCharCollectionInterface {

            override fun add(key: Pair<Int, Pair<Int, Int>>, info: BleChrateristicInfo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    series.put(key, SimpleXYSeries(info.characteristicName))

                    series.get(key)!!.useImplicitXVals()
                    val pointLabelFormatter=PointLabelFormatter()

                    plot!!.addSeries(series.get(key),  LineAndPointFormatter(getRandomColor(),0,0,PointLabelFormatter(Color.TRANSPARENT)))
                    listview_selected_char.adapter = MyAdapter()
                }
            }

            override fun remove(key: Pair<Int, Pair<Int, Int>>) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    plot.removeSeries(series.get(key))
                    series.remove(key)
                    listview_selected_char.adapter = MyAdapter()
                }
            }

        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notifyCharacteristicChange()
        }


    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        println(multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.characteristicName)

        val isStandardCharacteristic=multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.standardCharacteristic

        if(isStandardCharacteristic){
            var dialog = DialogStandardCharateristicsSettings(this, object : DialogCharateristicSettingsInterface {
                override fun onSaveClick(data: String) {
                    try {
                        multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation = data
                        println(multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
                        listview_selected_char.adapter = MyAdapter()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }, multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
            dialog.show()

        }else{
            var dialog = DialogCharacteristicSettings(this, object : DialogCharateristicSettingsInterface {
                override fun onSaveClick(data: String) {
                    try {
                        multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation = data
                        println(multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
                        listview_selected_char.adapter = MyAdapter()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }, multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
            dialog.show()
        }


    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        // notifyCharacteristicChange()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun notifyCharacteristicChange() {
        series.clear()
        plot.clear()
        createPlotFormat()
        if (redrawer != null) {
            redrawer!!.finish()
        }
        redrawer = Redrawer(plot, 20f, false)
        multiDeviceCharCollection.forEach { key, value ->

            series.put(Pair(key.first, Pair(key.second.first, key.second.second)), SimpleXYSeries(value.characteristicName))
            series.get(Pair(key.first, Pair(key.second.first, key.second.second)))!!.useImplicitXVals();
//            val pointLabelFormatter=PointLabelFormatter()
//            pointLabelFormatter.
            plot!!.addSeries(series.get(Pair(key.first, Pair(key.second.first, key.second.second))), LineAndPointFormatter(getRandomColor(),0,0,PointLabelFormatter(Color.TRANSPARENT)))
        }
        redrawer!!.start()
        listview_selected_char.adapter = MyAdapter()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        // redrawer!!.pause()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            counter++
            if (action == ACTION_SEND_CHARACTERISTIC_VALUE) {

                val deviceIndex = intent.getIntExtra(EXTRA_DEVICE_ID, 0)
                val serviceindex = intent.getIntExtra(EXTRA_SERVICE_INDEX, 0)
                val charateristicIndex = intent.getIntExtra(EXTRA_CHARACTERISTIC_INDEX, 0)
                try {
                    val data = intent.getByteArrayExtra(EXTRA_DATA)
                    val seria = series.get(key = Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                    val chrateristicInfo = multiDeviceCharCollection.get(Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                    if (chrateristicInfo != null) {
                        var rule = chrateristicInfo!!.dataRoleInterpratation
                        updateCharateristic(seria, deviceIndex, serviceindex, charateristicIndex, data, rule)
                    }
                } catch (e: Exception) {
                    val data = intent.getDoubleExtra(EXTRA_DATA, Double.POSITIVE_INFINITY)
                    if(data==Double.POSITIVE_INFINITY){
                        return
                    }
                    val seria = series.get(key = Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                    val chrateristicInfo = multiDeviceCharCollection.get(Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                    if (chrateristicInfo != null) {
                        var rule = chrateristicInfo!!.dataRoleInterpratation
                        updateCharateristicAsDouble(seria, deviceIndex, serviceindex, charateristicIndex, data,rule)
                    }
                }


            }
        }
    }

    private fun updateCharateristicAsDouble(seria: SimpleXYSeries?, deviceIndex: Int, serviceindex: Int, charateristicIndex: Int, data: Double, rule: String) {

        if (seria != null) {
            if(rule!="NONE"){
                seria.addLast(null, data)
                while (seria.size() > historySize) {
                    seria.removeFirst()
                }

            }else{
                seria.clear()
            }

        } else {
            println("series for:" + deviceIndex + " " + serviceindex + " " + charateristicIndex + " is null")
        }
    }

    private fun updateCharateristic(seria: SimpleXYSeries?, deviceIndex: Int, serviceindex: Int, charateristicIndex: Int, data: ByteArray, rule: String) {
        if (seria != null) {
            var values = convertData(data, rule)
            if (values != null) {
                for (i in 0 until values.size) {
                    //   println ("value "+values[i])
                    seria.addLast(null, values[i])
                    while (seria.size() > historySize) {
                        seria.removeFirst()
                    }

                }
            } else {
                seria.clear()
            }
        } else {
            println("series for:" + deviceIndex + " " + serviceindex + " " + charateristicIndex + " is null")
        }
    }

    private fun convertData(data: ByteArray, rule: String): DoubleArray? {

        when (rule) {
            "NONE" -> return null
            "STRING" -> return interpretAsString(data)
            "INTEGER" -> return interpretAsInteger(data)
            "FLOAT" -> return interpretAsFloat(data)
            else -> return null
        }
    }


    inner class MyHolder {
        var deviceName: TextView? = null
        var characteristicName: TextView? = null
        var interpretation: TextView? = null
        var characteristicUUID: TextView? = null
    }

    inner class MyAdapter : BaseAdapter() {

        private val mInflator: LayoutInflater = layoutInflater
        var kays = ArrayList(multiDeviceCharCollection.keys)


        override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {

            var view = mInflator.inflate(R.layout.list_item_selected_characteristics, null)
            var myHolder = MyHolder()
            myHolder.deviceName = view.findViewById(R.id.tv_device_name)
            myHolder.interpretation = view.findViewById(R.id.tv_interpret)
            myHolder.characteristicName = view.findViewById(R.id.tv_characteristic_name)
            myHolder.characteristicUUID = view.findViewById(R.id.tv_characteristic_uuid)

            myHolder.deviceName!!.text = multiDeviceCharCollection.get(kays.get(i))!!.deviceName

            myHolder.characteristicName!!.text = multiDeviceCharCollection.get(kays.get(i))!!.characteristicName
            myHolder.characteristicUUID!!.text = "UUID: " + multiDeviceCharCollection.get(kays.get(i))!!.characteristicUIID
            myHolder.interpretation!!.text = "Interpret binary data as: " + multiDeviceCharCollection.get(kays.get(i))!!.dataRoleInterpratation

            return view

        }

        override fun getItem(i: Int): BleChrateristicInfo {
            return multiDeviceCharCollection.get(kays.get(i))!!
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getCount(): Int {
            return kays.size
        }
    }

    companion object {
        val ACTION_SEND_CHARACTERISTIC_VALUE = "ACTION_SEND_CHARACTERISTIC_VALUE"
        val EXTRA_DATA = "EXTRA_DATA"
        val EXTRA_SERVICE_INDEX = "EXTRA_SERVICE_INDEX"
        val EXTRA_CHARACTERISTIC_INDEX = "EXTRA_CHARACTERISTIC_INDEX"
        val EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID"
        val EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME"
        val EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS"
        val EXTRA_SERVICE_NAME = "EXTRA_SERVICE_NAME"
        val EXTRA_CHARACTERISTIC_NAME = "EXTRA_CHARACTERISTIC_NAME"

    }

    private fun makeIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()

        intentFilter.addAction(ACTION_SEND_CHARACTERISTIC_VALUE)

        return intentFilter
    }

    private fun createPlotFormat() {


        plot!!.setRangeBoundaries(0, BoundaryMode.AUTO, 10, BoundaryMode.AUTO)

        plot!!.setRangeStep(StepMode.SUBDIVIDE, 11.0)
     //  plot!!.linesPerRangeLabel = 1

        plot!!.domainTitle.pack()
        plot!!.rangeTitle.pack()



        plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
        plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.LEFT).format = DecimalFormat("0.#")

        plot!!.setDomainBoundaries(0, historySize, BoundaryMode.FIXED)
        plot!!.setDomainStep(StepMode.SUBDIVIDE, 11.0)

        plot!!.calculateMinMaxVals()

        val paint = Paint()
        paint.strokeWidth = 2f
        plot!!.graph.rangeGridLinePaint = paint
        plot!!.graph.domainGridLinePaint = paint
        plot!!.graph.domainOriginLinePaint = paint
        plot!!.graph.rangeOriginLinePaint = paint


        plot!!.legend.textPaint.textSize = 18f
        plot!!.legend.textPaint.color = Color.argb(255, 20, 20, 20)

        val sizeMetric = SizeMetric(18f, SizeMode.ABSOLUTE)

        plot!!.legend.iconSize.height = sizeMetric
        plot!!.legend.iconSize.width = sizeMetric


        plot!!.legend.marginTop = 50f
        val horizontalPosition = plot!!.legend.positionMetrics.xPositionMetric.layoutType
        plot!!.legend.positionMetrics.xPositionMetric.set(130f, horizontalPosition)
        plot!!.legend.setWidth(700f, SizeMode.ABSOLUTE)
        showGrid()
    }

    internal fun showGrid() {


        //lineLabelTextColorLeft
        var paint = plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.BOTTOM).paint
        paint.color = Color.argb(255, 20, 20, 20)
        //        plot.getGraph().getLineLabelStyle(
        //                XYGraphWidget.Edge.BOTTOM).setPaint(paint);

        paint = plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.LEFT).paint
        paint.color = Color.argb(255, 20, 20, 20)
        //        plot.getGraph().getLineLabelStyle(
        //                XYGraphWidget.Edge.LEFT).setPaint(paint);

        plot!!.graph.rangeGridLinePaint.color = Color.argb(255, 200, 200, 200)
        plot!!.graph.domainGridLinePaint.color = Color.argb(255, 200, 200, 200)
        plot!!.graph.domainOriginLinePaint.color = Color.argb(255, 200, 200, 200)
        plot!!.graph.rangeOriginLinePaint.color = Color.argb(255, 200, 200, 200)
        plot!!.legend.isVisible = true

    }

    internal fun hideGrid() {
        plot!!.graph.domainGridLinePaint.color = Color.TRANSPARENT
        plot!!.graph.domainGridLinePaint.color = Color.TRANSPARENT
        plot!!.graph.rangeOriginLinePaint.color = Color.TRANSPARENT
        plot!!.graph.domainOriginLinePaint.color = Color.TRANSPARENT


        var paint = plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.BOTTOM).paint
        paint.color = Color.TRANSPARENT
        //        plot.getGraph().getLineLabelStyle(
        //                XYGraphWidget.Edge.BOTTOM).setPaint(paint);

        paint = plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.LEFT).paint
        paint.color = Color.TRANSPARENT
        plot!!.legend.isVisible = false
        //        plot.getGraph().getLineLabelStyle(
        //                XYGraphWidget.Edge.LEFT).setPaint(paint);

    }
}