package com.izaphe.blemanager.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile.*
import android.content.*
import android.graphics.Paint
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*

import java.util.ArrayList
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.fragment_ble_services.*

import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import com.i7xaphe.blemanager.R
import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


import com.izaphe.ble.utils.BleUtils
import com.izaphe.blemanager.activities.GraphActivity
import com.izaphe.blemanager.activities.MainActivity
import com.izaphe.blemanager.ble.BleChrateristicInfo
import com.izaphe.blemanager.ble.BleDevice
import com.izaphe.blemanager.ble.MultiDeviceCharCollection
import com.izaphe.blemanager.ble.Utils.getPropertiesTextViews
import com.izaphe.blemanager.myinterfaces.MyOnChildClickListener
import com.izaphe.blemanager.services.ServiceBle
import com.izaphe.blemanager.views.TouchableImageButton


class FragmentBleServices : Fragment(), MyOnChildClickListener {

    private var deviceName: String? = null
    private var deviceAddress: String? = null
    private var tabIndex: Int? = null
    private var deviceID: Int? = null
    private var serviceBle: ServiceBle? = null
    var connected = STATE_DISCONNECTED

    var expanderListAdapter: ExpandableListAdapter? = null


    // Code to manage Service lifecycle.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            serviceBle = (service as ServiceBle.LocalBinder).serviceBle
            if (!serviceBle!!.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
                return
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            serviceBle = null
        }
    }

    // Handles various events fired by the Service.
    // EXTRA_DEVICE_ID: check if data comes from the device belonging to this fragment
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private val mGattUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            //check if data comes from the device belonging to this fragment
            if (intent.getIntExtra(ServiceBle.EXTRA_DEVICE_ID, -1) == deviceID) {
                if (ServiceBle.ACTION_GATT_CONNECTED == action) {
                    connected = STATE_CONNECTED
                    //next onClickAction for toolbartextView ill be disconnect
                    updateToolbarAction(R.string.disconnect)
                } else if (ServiceBle.ACTION_GATT_CONNECTING == action) {
                    connected = STATE_CONNECTING
                    //next onClickAction for toolbartextView ill be connect
                    updateToolbarAction(R.string.connecting)
                } else if (ServiceBle.ACTION_GATT_DISCONNECTED == action) {
                    connected = STATE_DISCONNECTED

                    try {
                        for (i in 0 until (expanderListAdapter as MyExpanderListAdapter).mLeServices.size) {
                            for (k in 0 until (expanderListAdapter as MyExpanderListAdapter).mLeCharacteristic.get(i).size) {
                                MultiDeviceCharCollection.removeMultiDeviceCharCollection(Pair(deviceID!!, Pair(i, k)))
                            }
                        }

                    } catch (e: Exception ) {
                        e.printStackTrace()
                    } finally {
                        updateToolbarAction(R.string.connect)
                        clearUI()
                    }

                } else if (ServiceBle.ACTION_GATT_DISCONNECTING == action) {
                    connected = STATE_DISCONNECTING
                    //next onClickAction for toolbartextView ill be connect
                    //updateToolbarAction(R.string.disconnecting)

                } else if (ServiceBle.ACTION_GATT_SERVICES_DISCOVERED == action) {
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(serviceBle!!.getBluetoothDevice(deviceID!!))
                } else if (ServiceBle.ACTION_DATA_AVAILABLE == action) {
                    //        Log.i(TAG, "ACTION_DATA_AVAILABLE onReceive")
                    val type = intent.extras.getString(ServiceBle.EXTRA_ACTION_TYPE)
                    displayData(intent.getByteArrayExtra(ServiceBle.EXTRA_CHARATERISTIC_DATA),
                            //service index informs which service incoming data belongs to
                            intent.getIntExtra(ServiceBle.EXTRA_SERVICE_INDEX, 0),
                            //characteristic index informs which characteristic incoming data belongs to
                            intent.getIntExtra(ServiceBle.EXTRA_CHARATERISTIC_INDEX, 0), type)
                }
            }

        }
    }


    private fun clearUI() {
        gatt_services_list!!.setAdapter(null as SimpleExpandableListAdapter?)

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater!!.inflate(R.layout.fragment_ble_services, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()
        val bundle = this.arguments
        if (bundle != null) {
            deviceName = bundle.getString(EXTRAS_DEVICE_NAME)
            deviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS)
            tabIndex = bundle.getInt(EXTRAS_TAB_INDEX)
            deviceID = tabIndex!! - 1
        }

        val lp = ib_close.layoutParams as LayoutParams

        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        ib_close.layoutParams = lp
        ib_close.setOnClickListener({
            ib_close.startAnimation(AnimationUtils.loadAnimation(context, R.anim.propery_click))

            //      (activity as MainActivity).closeTab(tabIndex!!)
        })

        //Bind service
        val gattServiceIntent = Intent(context, ServiceBle::class.java)
        gattServiceIntent.action = tabIndex.toString()
        if (activity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            Log.i(TAG, "ble service ok")
        } else {
            Log.i(TAG, "ble service error")
        }

    }


    override fun onResume() {
        super.onResume()
        activity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())

    }

    //function  to connect to ble device
    //can be use externally  by activity
    fun connect(): Boolean {

        if (serviceBle != null) {
            return serviceBle!!.connect(deviceAddress, deviceID!!)
        } else {
            val gattServiceIntent = Intent(context, ServiceBle::class.java)
            gattServiceIntent.action = tabIndex.toString()
            if (activity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
                Log.i(TAG, "ble service ok")
            } else {
                Log.i(TAG, "ble service error")
            }
            return false
        }

    }

    //function  to disconnect to ble device
    //can be use externally by activity
    fun disconnect() {
        serviceBle!!.disconnect(deviceID!!)

    }


    private fun updateToolbarAction(resourceId: Int) {
        (activity as MainActivity).overwriteToolbarTextView(tabIndex!!, getString(resourceId))
    }


    private fun displayData(data: ByteArray?, groupPos: Int, childPos: Int, type: String?) {

        try {
            (expanderListAdapter as MyExpanderListAdapter).updateCharateristicValue(groupPos, childPos, data!!)
            Thread(Runnable {
                if ((expanderListAdapter as MyExpanderListAdapter).getChild(groupPos, childPos)!!.findViewById<ImageButton>(R.id.ib_graph).tag == 1) {

                    try {
                        val intent = Intent()
                        intent.action = GraphActivity.ACTION_SEND_CHARACTERISTIC_VALUE
                        intent.putExtra(GraphActivity.EXTRA_DEVICE_NAME, deviceName)
                        intent.putExtra(GraphActivity.EXTRA_DEVICE_ADDRESS, deviceAddress)
                        intent.putExtra(GraphActivity.EXTRA_SERVICE_NAME, (expanderListAdapter as MyExpanderListAdapter).getGroup(groupPos)!!.findViewById<TextView>(R.id.tv_interpret).text.toString())
                        intent.putExtra(GraphActivity.EXTRA_CHARACTERISTIC_NAME, (expanderListAdapter as MyExpanderListAdapter).getChild(groupPos, childPos)!!.findViewById<TextView>(R.id.tv_characteristic_name).text.toString())
                        intent.putExtra(GraphActivity.EXTRA_SERVICE_INDEX, groupPos)
                        intent.putExtra(GraphActivity.EXTRA_CHARACTERISTIC_INDEX, childPos)
                        intent.putExtra(GraphActivity.EXTRA_DEVICE_ID, deviceID)
                        val dataAsDouble=(expanderListAdapter as MyExpanderListAdapter).getCharateristicValueAsDouble(groupPos,childPos,data)
                        if(dataAsDouble==null){
                            //put data as ByteArray
                            intent.putExtra(GraphActivity.EXTRA_DATA, data)
                        }else{
                            //put data as Double
                            intent.putExtra(GraphActivity.EXTRA_DATA, dataAsDouble)
                        }
                        (activity as MainActivity).sendBroadcast(intent)
                    } catch (e: ClassCastException) {
                        e.printStackTrace()
                    }

                }
            }).start()
            // (expanderListAdapter as MyExpanderListAdapter).notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // elvGattServicesList!!.no
    }

    // Display gattServices and gattCharacteristics in ExpandedListView
    private fun displayGattServices(bleDevice: BleDevice) {
        expanderListAdapter = MyExpanderListAdapter(context, bleDevice.mListServices!!, bleDevice.mListCharacteristic, this)
        gatt_services_list!!.setAdapter(expanderListAdapter)
    }

    //View holder for GroupView in MyExpanderListAdapter
    internal class GroupViewHolder {
        //serviceName holds service name
        var serviceName: TextView? = null
        //serviceUUID holds service UIID
        var serviceUUID: TextView? = null
        //serviceType holds service Type (primary or secondary)
        var serviceType: TextView? = null
    }


    override fun onChildClick(v: View?, groupPosition: Int, childPosition: Int, Id: Long?, property: String?) {
        Log.i("onChildClick", "" + groupPosition + childPosition + id)

        if (v == null) {
            return
        }
        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.propery_click))
        when (property) {
            "BROADCAST" -> {
                Toast.makeText(context, "PROPERTY NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show()
            }
            "READ" -> {
                serviceBle!!.readCharacteristic(deviceID!!, (v.tag as Pair<Int, Int>).first, (v.tag as Pair<Int, Int>).second)

            }
            "WRITE NO RESPONSE" -> {
                val inflater = layoutInflater
                val dialoglayout = inflater.inflate(R.layout.dialog_write_property, null)
                val builder = AlertDialog.Builder(context)
                var data = dialoglayout.findViewById<TextInputEditText>(R.id.tiet_data)

                builder.setView(dialoglayout)
                builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                        dialog!!.dismiss()
                    }

                })
                builder.setPositiveButton("Send", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                        serviceBle!!.writeCharacteristic(deviceID!!, (v.tag as Pair<Int, Int>).first, (v.tag as Pair<Int, Int>).second, data.text.toString())
                        //update text hare because no respond from service
                        // childViewHolder.tvCharacteristicValue!!.text = data.text.toString()
                        dialog!!.dismiss()
                    }

                })
                builder.show()

            }
            "WRITE" -> {
                val inflater = layoutInflater
                val dialoglayout = inflater.inflate(R.layout.dialog_write_property, null)
                val builder = AlertDialog.Builder(context)
                var data = dialoglayout.findViewById<TextInputEditText>(R.id.tiet_data)

                builder.setView(dialoglayout)
                builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                        dialog!!.dismiss()
                    }

                })
                builder.setPositiveButton("Send", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                        serviceBle!!.writeCharacteristic(deviceID!!, (v.tag as Pair<Int, Int>).first, (v.tag as Pair<Int, Int>).second, data.text.toString())
                        dialog!!.dismiss()
                    }

                })
                builder.show()

            }
            "NOTIFY" -> {
                if ((v as TextView).paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0) {
                    serviceBle!!.setCharacteristicNotification(deviceID!!, (v.getTag() as Pair<Int, Int>).first, (v.getTag() as Pair<Int, Int>).second, false, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
                    v.paintFlags = v.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()


                } else {
                    serviceBle!!.setCharacteristicNotification(deviceID!!, (v.getTag() as Pair<Int, Int>).first, (v.getTag() as Pair<Int, Int>).second, true, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                    v.paintFlags = v.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                }

            }
            "INDICATE" -> {
                if ((v as TextView).paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0) {
                    serviceBle!!.setCharacteristicNotification(deviceID!!, (v.getTag() as Pair<Int, Int>).first, (v.getTag() as Pair<Int, Int>).second, false, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
                    v.paintFlags = v.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

                } else {
                    serviceBle!!.setCharacteristicNotification(deviceID!!, (v.getTag() as Pair<Int, Int>).first, (v.getTag() as Pair<Int, Int>).second, true, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
                    v.paintFlags = v.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                }
            }
            "SIGNED WRITE" -> {
                Toast.makeText(context, "PROPERTY NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show()
            }
            "EXTENDED PROPS" -> {
                Toast.makeText(context, "PROPERTY NOT IMPLEMENTED YET", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "WRONG PROPERTY", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //View holder for ChildView in MyExpanderListAdapter
    internal class ChildViewHolder {
        //charName holds characteristic name
        var charName: TextView? = null
        //charName holds characteristic UIID
        var charUUID: TextView? = null
        // propertiesLinearlayout holds dynamically added clickable characteristic properties
        var propertiesLinearlayout: LinearLayout? = null
        // tvCharacteristicValue holds characteristic value
        var tvCharacteristicValue: TextView? = null
        // tvCharacteristicValue holds characteristic value
        var ibGraph: TouchableImageButton? = null


    }

    private inner class MyExpanderListAdapter(context: Context,
                                              mLeServices: List<BluetoothGattService>,
                                              mLeCharacteristic: ArrayList<List<BluetoothGattCharacteristic>>,
                                              listener: MyOnChildClickListener) : BaseExpandableListAdapter() {

        private var context: Context = context
        private val mInflator: LayoutInflater = this@FragmentBleServices.layoutInflater
        private val listener = listener
        //List  of available service
        val mLeServices = mLeServices
        // list of ArrayList of available characteristic
        val mLeCharacteristic = mLeCharacteristic
        //childViews holds views which store the values of the characteristic.
        //these values are modified externally by Fragment in mGattUpdateReceiver
        private val childViews: HashMap<Pair<Int, Int>, View> = HashMap()
        //characteristicConverters store objects for converting supported standard  bla characteristics
        private val characteristicConverters: HashMap<Pair<Int, Int>, AbstractBleCharacteristic?> = HashMap()
        private val groupViews: HashMap<Int, View> = HashMap()


        override fun getGroupCount(): Int {
            return mLeServices.size
        }

        override fun getChildrenCount(i: Int): Int {
            return mLeCharacteristic.get(i).size
        }

        override fun getGroup(i: Int): View? {
            return groupViews.get(i)
        }

        override fun getChild(groupPos: Int, childPos: Int): View? {
            return childViews.get(Pair(groupPos, childPos))
        }

        override fun getGroupId(groupPos: Int): Long {
            return groupPos.toLong()

        }

        override fun getChildId(groupPos: Int, childPos: Int): Long {
            try {
                return (" " + groupPos + childPos + childViews.get(Pair(groupPos, childPos))!!.findViewById<TextView>(R.id.tv_characteristic_value).text.toString()).hashCode().toLong()
            } catch (e: NullPointerException) {
                return (" " + groupPos + childPos).hashCode().toLong()
            }
        }

        override fun hasStableIds(): Boolean {
            //all vies all hold in hashmap and reload fom here
            return false
        }

        @SuppressLint("SetTextI18n")
        override fun getGroupView(groupPos: Int, p1: Boolean, view: View?, p3: ViewGroup?): View {


            if (groupViews.contains(groupPos)) {
                //return view from HashMap childViews
                return getGroup(groupPos)!!
            } else {

                var v = mInflator.inflate(R.layout.list_item_expanded_list_group, null)
                val groupViewHolder: FragmentBleServices.GroupViewHolder
                groupViewHolder = FragmentBleServices.GroupViewHolder()
                groupViewHolder.serviceName = v!!.findViewById(R.id.tv_interpret)
                groupViewHolder.serviceUUID = v.findViewById(R.id.tv_service_uuid)
                groupViewHolder.serviceType = v.findViewById(R.id.tv_service_type)
                groupViewHolder.serviceName!!.text = BleUtils.getServiceName(mLeServices.get(groupPos).uuid.toString())
                groupViewHolder.serviceUUID!!.text = "UUID: " + BleUtils.UIIDFilter(mLeServices.get(groupPos).uuid.toString())
                groupViewHolder.serviceType!!.text = if (mLeServices.get(groupPos).type == 0) getString(R.string.primary_srvice) else getString(R.string.secondary_service)
                groupViews.put(groupPos, v)
                return v

            }


        }

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        override fun getChildView(groupPos: Int, childPos: Int, p2: Boolean, view: View?, parent: ViewGroup?): View {

            println("getChildView $groupPos $childPos")

            if (childViews.contains(Pair(groupPos, childPos))) {
                //return view from HashMap childViews
                return getChild(groupPos, childPos)!!
            } else {
                //create new view

                val v = mInflator.inflate(R.layout.list_item_expanded_list_child, null)
                val childViewHolder = ChildViewHolder()

                childViewHolder.charName = v!!.findViewById(R.id.tv_characteristic_name)
                childViewHolder.charUUID = v.findViewById(R.id.tv_characteristic_uuid)
                childViewHolder.propertiesLinearlayout = v.findViewById(R.id.ll_properties)
                childViewHolder.tvCharacteristicValue = v.findViewById(R.id.tv_characteristic_value)


                childViewHolder.ibGraph = v.findViewById(R.id.ib_graph)
                childViewHolder.ibGraph!!.tag = 0
                childViewHolder.ibGraph!!.setOnClickListener({

                })
                childViewHolder.ibGraph!!.setOnTouchListener(View.OnTouchListener({ view, motionEvent ->


                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.propery_click))
                        if (Integer.parseInt((view as ImageButton).tag.toString()) == 0) {
                            view.setImageResource(R.drawable.graph_icon_strike)
                            view.setTag(R.drawable.graph_icon_strike)
                            //use Tag to know if data should be broadcast
                            //broadcast data
                            view.setTag(1)

                            val UUID=BleUtils.UIIDFilter(mLeCharacteristic.get(groupPos).get(childPos).uuid.toString())
                            val standardCharacteristic= UUID.length==4
                            val characteristicName= childViews.get(Pair(groupPos, childPos))!!.findViewById<TextView>(R.id.tv_characteristic_name).text.toString()
                            val characteristicIndex=Pair(groupPos, childPos)
                            val characteristicInfo=BleChrateristicInfo(deviceName!!,characteristicName,UUID,standardCharacteristic)
                            MultiDeviceCharCollection.addtoMultiDeviceCharCollection(Pair(deviceID!!,characteristicIndex),characteristicInfo)
                            //    notifyDataSetChanged()

                        } else {

                            view.setImageResource(R.drawable.graph_icon)
                            view.tag = R.drawable.graph_icon
                            //use Tag to know if data should be broadcast
                            view.tag = 0
                            MultiDeviceCharCollection.removeMultiDeviceCharCollection(Pair(deviceID!!, Pair(groupPos, childPos)))
                            //    notifyDataSetChanged()

                        }
                    }
                    true

                }))


                childViewHolder.charName!!.text = BleUtils.getCharateristicName(mLeCharacteristic.get(groupPos).get(childPos).uuid.toString())
                childViewHolder.charUUID!!.text = """UUID: ${BleUtils.UIIDFilter(mLeCharacteristic.get(groupPos).get(childPos).uuid.toString())}"""

                childViewHolder.propertiesLinearlayout = v.findViewById(R.id.ll_properties)

                val packetConverter: AbstractBleCharacteristic? = BleUtils.getCharacteristicObject(mLeCharacteristic.get(groupPos).get(childPos).uuid.toString())

                characteristicConverters.put(Pair(groupPos, childPos), packetConverter)


                //dynamically added clickable textViews that correspond to the properties of characteristic
                for (tv in getPropertiesTextViews(mLeCharacteristic.get(groupPos).get(childPos).properties, context)) {

                    if (tv.text.toString() == "NOTIFY" || tv.text.toString() == "INDICATE") {
                        childViewHolder.ibGraph!!.visibility = View.VISIBLE
                    }
                    tv.tag = Pair(groupPos, childPos)

                    tv.setOnTouchListener(View.OnTouchListener({ view, motionEvent ->

                        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                            val property = (view as TextView).text.toString()
                            //onChildClick  depending on the property
                            listener.onChildClick(tv, (view.tag as Pair<Int, Int>).first, (view.tag as Pair<Int, Int>).second, null, property)

                        }
                        true

                    }))



                    childViewHolder.propertiesLinearlayout!!.addView(tv)
                }


                //store view in HashMap
                childViews.put(Pair(groupPos, childPos), v)
                return v
            }


        }

        override fun isChildSelectable(p0: Int, p1: Int): Boolean {
            return true
        }

        fun updateCharateristicValue(groupPos: Int, childPos: Int, data: ByteArray) {
            val packetConverter = characteristicConverters.get(Pair(groupPos, childPos))
            if (packetConverter != null) {
                packetConverter.packet = data
                val convertedData = packetConverter.getValueAsString()
                this.getChild(groupPos, childPos)!!.findViewById<TextView>(R.id.tv_characteristic_value).visibility = View.VISIBLE
                this.getChild(groupPos, childPos)!!.findViewById<TextView>(R.id.tv_characteristic_value).text = convertedData

            } else {
                this.getChild(groupPos, childPos)!!.findViewById<TextView>(R.id.tv_characteristic_value).visibility = View.VISIBLE
                this.getChild(groupPos, childPos)!!.findViewById<TextView>(R.id.tv_characteristic_value).text =  BleUtils.byteArrayToHex(data)
            }
        }

        fun getCharateristicValueAsDouble(groupPos: Int, childPos: Int, data: ByteArray):Double? {
            val packetConverter = characteristicConverters.get(Pair(groupPos, childPos))
            if (packetConverter != null) {
                packetConverter.packet = data
                val convertedData = packetConverter.getValueAsDouble()
                return convertedData
            }
            return null
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()

        intentFilter.addAction(ServiceBle.ACTION_GATT_CONNECTED)
        intentFilter.addAction(ServiceBle.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(ServiceBle.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(ServiceBle.ACTION_GATT_DISCONNECTING)
        intentFilter.addAction(ServiceBle.ACTION_GATT_CONNECTING)
        intentFilter.addAction(ServiceBle.ACTION_DATA_AVAILABLE)

        return intentFilter
    }


    companion object {
        private val TAG = FragmentBleServices::class.java.simpleName
        @JvmField
        var EXTRAS_DEVICE_NAME = "DEVICE_NAME"
        @JvmField
        var EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS"
        @JvmField
        var EXTRAS_TAB_INDEX = "TAB_INDEX"


    }
}
