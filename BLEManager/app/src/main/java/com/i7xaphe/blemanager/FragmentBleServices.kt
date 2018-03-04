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
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.*

import java.util.ArrayList
import java.util.HashMap

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with `ServiceBle`, which in turn interacts with the
 * Bluetooth LE API.
 */
class FragmentBleServices : Fragment() {
    private var tvConnectionState: TextView? = null
    private var tvData: TextView? = null
    private var deviceName: String? = null
    private var deviceAddress: String? = null
    private var elvGattServicesList: ExpandableListView? = null
    private var serviceBle: ServiceBle? = null

    private var connected = false
    private var bluetoothGattCharacteristic: BluetoothGattCharacteristic? = null

    private val LIST_NAME = "NAME"
    private val LIST_UUID = "UUID"


    // Code to manage Service lifecycle.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            serviceBle = (service as ServiceBle.LocalBinder).serviceBle
            if (!serviceBle!!.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
                return
            }
            // Automatically connects to the device upon successful start-up initialization.
            if(serviceBle!!.connect(deviceAddress)){
                Log.e(TAG, "Connected to BLE Device")
            }else{
                Log.e(TAG, "Unable connect to BLE Device")
            }
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
            if (ServiceBle.ACTION_GATT_CONNECTED == action) {
                connected = true
                updateConnectionState(R.string.connected)
            } else if (ServiceBle.ACTION_GATT_DISCONNECTED == action) {
                connected = false
                updateConnectionState(R.string.disconnected)
                clearUI()
            } else if (ServiceBle.ACTION_GATT_SERVICES_DISCOVERED == action) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(serviceBle!!.supportedGattServices)
            } else if (ServiceBle.ACTION_DATA_AVAILABLE == action) {
                displayData(intent.getStringExtra(ServiceBle.EXTRA_DATA))
            }
        }
    }

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private val servicesListClickListner = ExpandableListView.OnChildClickListener { parent, v, groupPosition, childPosition, id ->


        Log.d(TAG,"servicesListClickListner $groupPosition $childPosition "+v.id)

        return@OnChildClickListener true
       //false
    }

    private fun clearUI() {
        elvGattServicesList!!.setAdapter(null as SimpleExpandableListAdapter?)
        tvData!!.setText(R.string.no_data)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v=inflater!!.inflate(R.layout.fragment_ble_services, container, false)
        retainInstance = false
        val bundle = this.arguments
        if (bundle != null) {
            deviceName = bundle!!.getString(EXTRAS_DEVICE_NAME)
            deviceAddress = bundle!!.getString(EXTRAS_DEVICE_ADDRESS)
        }




        Log.i(TAG,"deviceName: "+deviceName)
        Log.i(TAG,"deviceName: "+deviceAddress)
        // Sets up UI references.

         v.findViewById<TextView>(R.id.device_address).text=deviceAddress
        elvGattServicesList = v.findViewById<ExpandableListView>(R.id.gatt_services_list)
        elvGattServicesList!!.setOnChildClickListener(servicesListClickListner)
        tvConnectionState = v.findViewById(R.id.connection_state)
        tvData = v.findViewById(R.id.data_value)


        var floatingactionbutton=v.findViewById<FloatingActionButton>(R.id.floatingactionbutton)

        floatingactionbutton.setOnClickListener { view ->
            Snackbar.make(view, "Graphical data visualization will be added soon", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val gattServiceIntent = Intent(context, ServiceBle::class.java)
        if(activity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)){
            Log.i(TAG,"ble service ok")
        }else{
            Log.i(TAG,"ble service error")
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        activity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (serviceBle != null) {
            val result = serviceBle!!.connect(deviceAddress)
            Log.d(TAG, "Connect request result=" + result)
        }else{

            Log.d(TAG, "serviceBle null")
        }
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



    private fun updateConnectionState(resourceId: Int) {
        tvConnectionState!!.setText(resourceId)
    }

    private fun displayData(data: String?) {

        tvData!!.text = data
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private fun displayGattServices(gattServices: List<BluetoothGattService>?) {

        var gattCharacteristic: ArrayList<List<BluetoothGattCharacteristic>>?=ArrayList()

        // Loops through available GATT Services.
        for (gattService in gattServices!!) {
            gattCharacteristic!!.add(gattService.characteristics)
        }

       var expanderListAdapter=ExpanderListAdapter(context,gattServices,gattCharacteristic)
        elvGattServicesList!!.setAdapter(expanderListAdapter)
        elvGattServicesList!!.setOnChildClickListener(servicesListClickListner)
    }

    internal class ViewHolder {
        var serviceName: TextView? = null
        var serviceUUID: TextView? = null
        var serviceType: TextView? = null
    }

    internal class ViewHolderChild {
        var charName: TextView? = null
        var charUUID: TextView? = null
        var button: TextView? = null
    }


    private inner class ExpanderListAdapter(context: Context,
                                            mLeServices:List<BluetoothGattService>,
                                            mLeCharacteristic: ArrayList<List<BluetoothGattCharacteristic>>?) : BaseExpandableListAdapter() {

        private var context:Context=context
        private val mLeServices= mLeServices
        private val mLeCharacteristic=mLeCharacteristic
        private val mInflator: LayoutInflater = this@FragmentBleServices.layoutInflater

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
            return  mLeCharacteristic!!.get(groupPos).get(childPos)
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

        override fun getGroupView(i: Int, p1: Boolean, view: View?, p3: ViewGroup?): View {
            var view = view
            val viewHolder: FragmentBleServices.ViewHolder
            if (view == null) {
                view = mInflator.inflate(R.layout.expandedlistitem, null)
                viewHolder = FragmentBleServices.ViewHolder()
                viewHolder.serviceName = view!!.findViewById(R.id.tv_service_name)
                viewHolder.serviceUUID = view.findViewById(R.id.tv_service_uuid)
                viewHolder.serviceType = view.findViewById(R.id.tv_service_type)
                view.tag = viewHolder
            } else {
                viewHolder = view.tag as FragmentBleServices.ViewHolder
            }
            viewHolder.serviceName!!.text=UUIDConverter().getServiceName(mLeServices.get(i).uuid.toString())
            viewHolder.serviceUUID!!.text="UUID: "+mLeServices.get(i).uuid.toString().substring(4,8)
            viewHolder.serviceType!!.text= if(mLeServices.get(i).type==0) getString(R.string.primary_srvice) else getString(R.string.secondary_service);


            return view
        }



        override fun getChildView(groupPos: Int, childPos: Int, p2: Boolean, view: View?, p4: ViewGroup?): View {

            var view = view
            val viewHolderChild: FragmentBleServices.ViewHolderChild
            if (view == null) {
                view = mInflator.inflate(R.layout.expandedlistitemchild, null)
                viewHolderChild = FragmentBleServices.ViewHolderChild()
                viewHolderChild.charName = view!!.findViewById(R.id.tv_characteristic_name)
                viewHolderChild.charUUID = view.findViewById(R.id.tv_characteristic_uuid)
                viewHolderChild.button = view.findViewById(R.id.buttonx)
                view.tag = viewHolderChild
            } else {
                viewHolderChild = view.tag as FragmentBleServices.ViewHolderChild
            }
            viewHolderChild.charName!!.text=UUIDConverter().getCharateristicName(mLeCharacteristic!!.get(groupPos).get(childPos).uuid.toString())
            viewHolderChild.charUUID!!.text="UUID: "+mLeCharacteristic!!.get(groupPos).get(childPos).uuid.toString().substring(4,8)

            return view

        }

        override fun isChildSelectable(p0: Int, p1: Int): Boolean {
            return true
        }


    }



    companion object {
        private val TAG = FragmentBleServices::class.java!!.getSimpleName()


       @JvmField var EXTRAS_DEVICE_NAME = "DEVICE_NAME"
       @JvmField var EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS"

        private fun makeGattUpdateIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ServiceBle.ACTION_GATT_CONNECTED)
            intentFilter.addAction(ServiceBle.ACTION_GATT_DISCONNECTED)
            intentFilter.addAction(ServiceBle.ACTION_GATT_SERVICES_DISCOVERED)
            intentFilter.addAction(ServiceBle.ACTION_DATA_AVAILABLE)
            return intentFilter
        }
    }


}
