package com.i7xaphe.blemanager

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
import android.widget.BaseAdapter
import android.widget.TextView
import com.i7xaphe.blemanager.MultiDeviceCharCollectionObserver.multiDeviceCharCollection

import android.widget.AdapterView.OnItemClickListener
import com.androidplot.ui.SizeMetric
import com.androidplot.ui.SizeMode
import com.androidplot.util.Redrawer
import com.androidplot.xy.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.DecimalFormat
import java.util.*
import kotlin.math.floor


/**
 * Created by Kamil on 2018-03-17.
 */
class GraphActivity : AppCompatActivity() {

    val context: Context = this
    var counter = 0
    private val series: HashMap<Pair<Int, Pair<Int, Int>>, SimpleXYSeries> = HashMap()
    private var redrawer: Redrawer? = null
    private var historySize = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        registerReceiver(broadcastReceiver, makeIntentFilter())

        listview_selected_char.onItemClickListener = OnItemClickListener { arg0, arg1, position, arg3
            ->
            println(multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.characteristicName)
            var dialog = DialogCharateristicSettings(this, object : DialogCharateristicSettingsInterface {
                override fun onSaveClick(data: String) {
                    try {
                        multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation = data
                        println(multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }, multiDeviceCharCollection.get((listview_selected_char.adapter as MyAdapter).kays[position])!!.dataRoleInterpratation)
            dialog.show()
        }

        plot.setOnClickListener({
            var dialog = DialogGraphSettings(this, object : DialogGraphSettingsInterface {
                override fun historySize(historySize: Int) {
                    plot!!.setDomainBoundaries(0, historySize, BoundaryMode.FIXED)
                    this@GraphActivity.historySize = historySize
                }
            }, historySize)
            dialog.show()
        })

        MultiDeviceCharCollectionObserver.setInterface(object : MultiDeviceCharCollectionInterface {

            override fun add(key: Pair<Int, Pair<Int, Int>>, info: GraphChrateristicInfo) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    series.put(key, SimpleXYSeries(info.characteristicName))
                    val series1Format = LineAndPointFormatter()
                    series.get(key)!!.useImplicitXVals()

                    series1Format.configure(context,
                            R.xml.led1_time)

                    plot!!.addSeries(series.get(key), series1Format)
                    listview_selected_char.adapter = MyAdapter()
                    println("Get notyfication")
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
            val series1Format = LineAndPointFormatter()
            series1Format.configure(this,
                    R.xml.led1_time)
            series.get(Pair(key.first, Pair(key.second.first, key.second.second)))!!.useImplicitXVals();
            plot!!.addSeries(series.get(Pair(key.first, Pair(key.second.first, key.second.second))), series1Format)
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
                val data = intent.getByteArrayExtra(EXTRA_DATA)

                val seria = series.get(key = Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                val chrateristicInfo = multiDeviceCharCollection.get(Pair(deviceIndex, Pair(serviceindex, charateristicIndex)))
                if (chrateristicInfo != null) {
                    var rule = chrateristicInfo!!.dataRoleInterpratation
                    updateCharateristic(seria, deviceIndex, serviceindex, charateristicIndex, data, rule)
                }

            }
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
            "NULL" -> return null
            "STRING" -> return interpretAsString(data)
            "INTEGER" -> return interpretAsInteger(data)
            "FLOAT" -> return interpretAsFloat(data)
            else -> return null
        }
    }

    private fun interpretAsString(data: ByteArray): DoubleArray {
        val string = String(data).replace("(\r\n|\t|\n\r|\r)".toRegex(), "\n").split("\n")
        var values = DoubleArray(string.size)
        for (i in 0 until string.size) {
            try {
                values[i] = string[i].toDouble()
            } catch (e: NumberFormatException) {
                values[i] = 0.0
            }
        }
        return values
    }

    private fun interpretAsInteger(data: ByteArray): DoubleArray {
        val size = data.size
        //  println("data: ByteArray"+Arrays.toString(data))
        var values = DoubleArray((floor((size / 4.0))).toInt())
        for (i in 0 until size step 4) {
            var sample = byteArrayOf(data[i], data[i + 1], data[i + 2], data[i + 3])
            //    println("sample: ByteArray"+Arrays.toString(sample))
            values[(i + 1) / 4] = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).getInt().toDouble()
            //     println("values: ByteArray"+Arrays.toString(values))
        }
        return values
    }

    private fun interpretAsFloat(data: ByteArray): DoubleArray {
        val size = data.size
        var values = DoubleArray((floor((size / 4.0))).toInt())
        for (i in 0 until size step 4) {
            var sample = byteArrayOf(data[i], data[i + 1], data[i + 2], data[i + 3])
            values[(i + 1) / 4] = ByteBuffer.wrap(sample).order(ByteOrder.LITTLE_ENDIAN).getFloat().toDouble()

        }
        return values
    }


    inner class MyHolder {
        var deviceName: TextView? = null
        var characteristicName: TextView? = null
        var serviceName: TextView? = null
    }

    inner class MyAdapter : BaseAdapter() {

        private val mInflator: LayoutInflater = layoutInflater
        var kays = ArrayList(multiDeviceCharCollection.keys)


        override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {

            var view = mInflator.inflate(R.layout.listitem_selsected_char, null)
            var myHolder = MyHolder()
            myHolder.deviceName = view.findViewById(R.id.tv_device_name)
            myHolder.serviceName = view.findViewById(R.id.tv_service_name)
            myHolder.characteristicName = view.findViewById(R.id.tv_characteristic_name)

            myHolder.deviceName!!.text = multiDeviceCharCollection.get(kays.get(i))!!.deviceName
            myHolder.serviceName!!.text = multiDeviceCharCollection.get(kays.get(i))!!.serviceName

            myHolder.characteristicName!!.text = multiDeviceCharCollection.get(kays.get(i))!!.characteristicName

            return view

        }

        override fun getItem(i: Int): GraphChrateristicInfo {
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

    internal fun createPlotFormat() {
        plot!!.setRangeBoundaries(0, 1200, BoundaryMode.AUTO)


        plot!!.setRangeStep(StepMode.SUBDIVIDE, 11.0)
        plot!!.linesPerRangeLabel = 1

        plot!!.domainTitle.pack()
        plot!!.rangeTitle.pack()

        plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.BOTTOM).format = DecimalFormat("#")
        plot!!.graph.getLineLabelStyle(
                XYGraphWidget.Edge.LEFT).format = DecimalFormat("0.#")

        plot!!.domainStepMode = StepMode.INCREMENT_BY_VAL
        plot!!.setDomainBoundaries(0, historySize, BoundaryMode.FIXED)
        plot!!.setDomainStep(StepMode.SUBDIVIDE, 11.0)

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