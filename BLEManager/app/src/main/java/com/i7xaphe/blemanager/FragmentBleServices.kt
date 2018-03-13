/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.i7xaphe.blemanager


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*

import java.util.ArrayList
import android.view.LayoutInflater


class FragmentBleServices : Fragment() {

    private var deviceName: String? = null
    private var deviceAddress: String? = null
    private var tabIndex: Int? = null
    private var bleDeviceIndex: Int? = null
    private var elvGattServicesList: ExpandableListView? = null
    private var serviceBle: ServiceBle? = null
    var connected = false

    var expanderListAdapter:ExpandableListAdapter?=null




    // Code to manage Service lifecycle.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            serviceBle = (service as ServiceBle.LocalBinder).serviceBle
            if (!serviceBle!!.initialize()) {

                Log.e(TAG, "Unable to initialize Bluetooth")
                return
            }

//            // Automatically connects to the device upon successful start-up initialization.
//            if (serviceBle!!.connect(deviceAddress)) {
//                Log.e(TAG, "Connected to BLE Device")
//            } else {
//                Log.e(TAG, "Unable connect to BLE Device")
//            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            serviceBle = null
        }
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private val mGattUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            Log.i(TAG, "BroadcastReceiver onReceive")
            println("intent.action=  "+intent.action)

            if(intent.getIntExtra(ServiceBle.DEVICE_ID,-1)==bleDeviceIndex) {
                if (ServiceBle.ACTION_GATT_CONNECTED == action) {
                    connected = true
                    //next onClickAction for toolbartextView ill be disconnect
                    updateToolbarAction(R.string.disconnect)
                } else if (ServiceBle.ACTION_GATT_DISCONNECTED == action) {
                    connected = false
                    //next onClickAction for toolbartextView ill be connect
                    updateToolbarAction(R.string.connect)
                    clearUI()
                } else if (ServiceBle.ACTION_GATT_SERVICES_DISCOVERED == action) {
                    // Show all the supported services and characteristics on the user interface.
                    displayGattServices(serviceBle!!.getBluetoothDevice(bleDeviceIndex!!))
                } else if (ServiceBle.ACTION_DATA_AVAILABLE == action) {
                    Log.i(TAG, "ACTION_DATA_AVAILABLE onReceive")

                    displayData(intent.getStringExtra(ServiceBle.EXTRA_DATA),
                            intent.getIntExtra(ServiceBle.SERVICE_INDEX, 0),
                            intent.getIntExtra(ServiceBle.CHARATERISTIC_INDEX, 0))
                }
            }

        }
    }

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private val servicesListClickListner = ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->


        Log.d(TAG, "servicesListClickListner $groupPosition $childPosition " + v.id)

        return@OnChildClickListener true
        //false
    }

    private fun clearUI() {
        elvGattServicesList!!.setAdapter(null as SimpleExpandableListAdapter?)

    }




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater!!.inflate(R.layout.fragment_ble_services, container, false)
        retainInstance = false
        val bundle = this.arguments
        if (bundle != null) {
            deviceName = bundle.getString(EXTRAS_DEVICE_NAME)
            deviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS)
            tabIndex = bundle.getInt(EXTRAS_TAB_INDEX)
            bleDeviceIndex= tabIndex!! -1
        }

        // Sets up UI references.
        elvGattServicesList = v.findViewById<ExpandableListView>(R.id.gatt_services_list)
        var floatingactionbutton = v.findViewById<FloatingActionButton>(R.id.floatingactionbutton)


        //add OnClick Listener to floatingactionbutton
        floatingactionbutton.setOnClickListener { view ->
            Snackbar.make(view, "Graphical data visualization will be added soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //Bind service
        val gattServiceIntent = Intent(context, ServiceBle::class.java)
        gattServiceIntent.action=tabIndex.toString()
        if (activity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
            Log.i(TAG, "ble service ok")
        } else {
            Log.i(TAG, "ble service error")
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        activity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (serviceBle != null) {
            val result = connect()
            Log.d(TAG, "Connect request result=" + result)
        }
    }
    //function  to connect to ble device
    //can be use externally  by activity
    fun connect():Boolean{
        return serviceBle!!.connect(deviceAddress,bleDeviceIndex!!)
    }

    //function  to disconnect to ble device
    //can be use externally by activity
    fun disconnect(){
         serviceBle!!.disconnect(bleDeviceIndex!!)
    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(mGattUpdateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.unbindService(mServiceConnection)
        serviceBle = null
    }


    private fun updateToolbarAction(resourceId: Int) {
        (activity as MainActivity).overwriteToolbarTextView(tabIndex!!,getString(resourceId))
    }

    private fun displayData(data: String?,groupPos: Int,childPos: Int) {

        Log.i(TAG, "displayData $data $groupPos $childPos")
        (expanderListAdapter as MyExpanderListAdapter).valueTextViewList!!.get(groupPos).get(childPos).visibility=View.VISIBLE
        (expanderListAdapter as MyExpanderListAdapter).valueTextViewList!!.get(groupPos).get(childPos).text=data
       // elvGattServicesList!!.no
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private fun displayGattServices(bleDevice: BleDevice) {
        expanderListAdapter = MyExpanderListAdapter(context, serviceBle!!.mListBleDevices.get(bleDeviceIndex!!)!!.mListServices!!,  serviceBle!!.mListBleDevices.get(bleDeviceIndex!!)!!.mListCharacteristic!!)
        elvGattServicesList!!.setAdapter(expanderListAdapter)
        elvGattServicesList!!.setOnChildClickListener(servicesListClickListner)
    }

    //View holder for GroupView in MyExpanderListAdapter
    internal class GroupViewHolder {
        var serviceName: TextView? = null
        var serviceUUID: TextView? = null
        var serviceType: TextView? = null
    }


    //View holder for ChildView in MyExpanderListAdapter
    internal class ChildViewHolder {
        var charName: TextView? = null
        var charUUID: TextView? = null
        //propertiesLinearlayout holds dynamically added clickable TextViews
        var propertiesLinearlayout: LinearLayout? = null
        var charValue:TextView?=null
    }



    private inner class MyExpanderListAdapter(context: Context,
                                              mLeServices: List<BluetoothGattService>,
                                              mLeCharacteristic: ArrayList<List<BluetoothGattCharacteristic>>) : BaseExpandableListAdapter() {

        private var context: Context = context
        private val mLeServices = mLeServices
        private val mLeCharacteristic = mLeCharacteristic
        private val mInflator: LayoutInflater = this@FragmentBleServices.layoutInflater
        var valueTextViewList:ArrayList<ArrayList<TextView>>?= ArrayList()


        override fun getGroupCount(): Int {
            return mLeServices.size
        }

        override fun getChildrenCount(i: Int): Int {
            return mLeCharacteristic!!.get(i).size
        }

        override fun getGroup(i: Int): Any {
            return mLeCharacteristic!!.get(i)
        }

        override fun getChild(groupPos: Int, childPos: Int): Any {
            return mLeCharacteristic!!.get(groupPos).get(childPos)
        }

        override fun getGroupId(id: Int): Long {
            return id.toLong()

        }

        override fun getChildId(groupPos: Int, childPos: Int): Long {
            return childPos.toLong()
        }

        override fun hasStableIds(): Boolean {
            return false
        }

        @SuppressLint("SetTextI18n")
        override fun getGroupView(groupPos: Int, p1: Boolean, view: View?, p3: ViewGroup?): View {
            var view = view
            val groupViewHolder: FragmentBleServices.GroupViewHolder
            if (view == null) {
                view = mInflator.inflate(R.layout.expandedlistitem, null)
                groupViewHolder = FragmentBleServices.GroupViewHolder()
                groupViewHolder.serviceName = view!!.findViewById(R.id.tv_service_name)
                groupViewHolder.serviceUUID = view.findViewById(R.id.tv_service_uuid)
                groupViewHolder.serviceType = view.findViewById(R.id.tv_service_type)
                view.tag = groupViewHolder
            } else {
                groupViewHolder = view.tag as FragmentBleServices.GroupViewHolder
            }
            groupViewHolder.serviceName!!.text = BLEConverter.getServiceName(mLeServices.get(groupPos).uuid.toString())
            groupViewHolder.serviceUUID!!.text = "UUID: " + BLEConverter.UIIDFilter(mLeServices.get(groupPos).uuid.toString())
            groupViewHolder.serviceType!!.text = if (mLeServices.get(groupPos).type == 0) getString(R.string.primary_srvice) else getString(R.string.secondary_service);
            if(valueTextViewList!!.size<=groupPos){
                valueTextViewList!!.add(groupPos,ArrayList())
            }


            return view
        }


        @SuppressLint("SetTextI18n")
        override fun getChildView(groupPos: Int, childPos: Int, p2: Boolean, view: View?, p4: ViewGroup?): View {


            println("getChildView $groupPos $childPos")

                var childViewHolder: FragmentBleServices.ChildViewHolder = FragmentBleServices.ChildViewHolder()

                var v = mInflator.inflate(R.layout.expandedlistitemchild, null)
                childViewHolder.charName = v!!.findViewById(R.id.tv_characteristic_name)
                childViewHolder.charUUID = v.findViewById(R.id.tv_characteristic_uuid)
                childViewHolder.propertiesLinearlayout = v.findViewById(R.id.ll_properties)
                if(valueTextViewList!!.get(groupPos).size<=childPos){
                    valueTextViewList!!.get(groupPos).add(childPos, v.findViewById(R.id.tv_characteristic_value))
                }else{
                    var lastVlue= valueTextViewList!!.get(groupPos).get(childPos).text
                    var visibility= valueTextViewList!!.get(groupPos).get(childPos).visibility
                    valueTextViewList!!.get(groupPos).set(childPos, v.findViewById(R.id.tv_characteristic_value))
                    valueTextViewList!!.get(groupPos).get(childPos).text=lastVlue
                    valueTextViewList!!.get(groupPos).get(childPos).visibility=visibility
                }
                childViewHolder.charValue= valueTextViewList!!.get(groupPos).get(childPos)
                for (tv in BLEConverter.getPropertiesView(mLeCharacteristic!!.get(groupPos).get(childPos).properties, context)) {
                    childViewHolder.propertiesLinearlayout!!.addView(tv)
                    tv.setOnClickListener(View.OnClickListener { textview ->
                        textview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.propery_click))
                        Log.i("clicked", tv.text.toString() + groupPos + childPos)
                        val property=tv.text.toString()
                        when(property){
                            "BROADCAST"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            "READ"->{
                           //     lastPropertyClicked=childViewHolder.charValue
                                serviceBle!!.readCharacteristic(groupPos,childPos,bleDeviceIndex!!)

                            }
                            "WRITE NO RESPONSE"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            "WRITE"->{
                                Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()
                                val inflater = layoutInflater
                                val dialoglayout = inflater.inflate(R.layout.alert_write_property, null)
                                val builder = AlertDialog.Builder(context)
                                var data = dialoglayout.findViewById<TextInputEditText>(R.id.tiet_data)

                                builder.setView(dialoglayout)
                                builder.setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                                        dialog!!.dismiss()
                                    }

                                })
                                builder.setPositiveButton("Send",object :DialogInterface.OnClickListener{
                                    override fun onClick(dialog: DialogInterface?, witch: Int) {
                                        serviceBle!!.writeCharacteristic(groupPos,childPos,bleDeviceIndex!!,data.text.toString())
                                        dialog!!.dismiss()
                                    }

                                })
                                builder.show()


                            }
                            "NOTIFY"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            "INDICATE"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            "SIGNED WRITE"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            "EXTENDED PROPS"->{Toast.makeText(context,"PROPERTY NOT IMPLEMENTED YET",Toast.LENGTH_SHORT).show()}
                            else->{Toast.makeText(context,"WRONG PROPERTY",Toast.LENGTH_SHORT).show()}

                        }
                    })
                }
                childViewHolder.charName!!.text = BLEConverter.getCharateristicName(mLeCharacteristic!!.get(groupPos).get(childPos).uuid.toString())
                childViewHolder.charUUID!!.text = "UUID: " + BLEConverter.UIIDFilter(mLeCharacteristic!!.get(groupPos).get(childPos).uuid.toString())

                return v
        }
        override fun isChildSelectable(p0: Int, p1: Int): Boolean {
            return true
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()

        intentFilter.addAction(ServiceBle.ACTION_GATT_CONNECTED)
        intentFilter.addAction(ServiceBle.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(ServiceBle.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(ServiceBle.ACTION_DATA_AVAILABLE)

        return intentFilter
    }






    companion object {
        private val TAG = FragmentBleServices::class.java!!.getSimpleName()
        @JvmField
        var EXTRAS_DEVICE_NAME = "DEVICE_NAME"
        @JvmField
        var EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS"
        @JvmField
        var EXTRAS_TAB_INDEX = "TAB_INDEX"



    }
}
