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

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.bluetooth.BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
import java.util.*


class ServiceBle : Service() {


    private val TAG = ServiceBle::class.java.simpleName


    var mListBleDevices: HashMap<Int,BleDevice> = HashMap()
    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val mBinder = LocalBinder()


    private fun broadcastUpdate(deviceID: Int,action: String) {
        val intent = Intent(action)
        intent.putExtra(EXTRA_DEVICE_ID,deviceID)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(deviceID: Int,action: String,
                                characteristic: BluetoothGattCharacteristic,pair: Pair<Int,Int>,type:String) {
        val intent = Intent(action)

        val data = characteristic.value
        intent.putExtra(EXTRA_ACTION_TYPE,type)
        intent.putExtra(EXTRA_DEVICE_ID,deviceID)
        intent.putExtra(EXTRA_CHARATERISTIC_DATA,data)
        intent.putExtra(EXTRA_SERVICE_INDEX,pair.first)
        intent.putExtra(EXTRA_CHARATERISTIC_INDEX,pair.second)
        sendBroadcast(intent)
    //    Log.w(TAG, "broadcastUpdate")
    }

    inner class LocalBinder : Binder() {
        internal val serviceBle: ServiceBle
            get() = this@ServiceBle
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }


    //called only once when service is created
    override fun onCreate() {
        super.onCreate()
        //initialize ble adapter
        initialize()

    }

    /**
     * Initializes a reference to the local Bluetooth adapter.

     * @return Return true if the initialization is successful.
     */
    fun initialize(): Boolean {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (bluetoothManager == null) {
            bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.")
                return false
            }
        }

        bluetoothAdapter = bluetoothManager!!.adapter
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        Log.e(TAG, "Initialize ble service OK")
        return true
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.

     * @param address The device address of the destination device.
     * @param deviceID device index in mListBleDevices.
     * *
     * @return Return true if the connection is initiated successfully. The connection result
     * *         is reported asynchronously through the
     * *         `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * *         callback.
     */
    fun connect(address: String?,deviceID:Int): Boolean {

        mListBleDevices.put(deviceID,BleDevice(deviceID,bluetoothAdapter!!.getRemoteDevice(address)))
      //  mListBleDevices.get(deviceID).deviceID=deviceID

        if ( mListBleDevices.get(deviceID)!!.device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.")
            return false
        }

        broadcastUpdate(deviceID,ACTION_GATT_CONNECTING)

        mListBleDevices.get(deviceID)!!.bluetoothGatt =  mListBleDevices.get(deviceID)!!.device.connectGatt(this, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    broadcastUpdate(deviceID,ACTION_GATT_CONNECTED)
                    Log.i(TAG, "Connected to GATT server.")
                    // Attempts to discover services after successful connection.
                    Log.i(TAG, "Attempting to start serviceBle discovery:" +  mListBleDevices.get(deviceID)!!.bluetoothGatt!!.discoverServices())

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

               //    mListBleDevices!!.get(deviceID)!!.connectionState  = STATE_DISCONNECTED
                    Log.i(TAG, "Disconnected from GATT server.")
                    broadcastUpdate(deviceID,ACTION_GATT_DISCONNECTED)
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    mListBleDevices.get(deviceID)!!.updateSetvices()

                    broadcastUpdate(deviceID,ACTION_GATT_SERVICES_DISCOVERED)
                } else {
                    Log.w(TAG, "onServicesDiscovered received: " + status)
                }
            }

            override fun onCharacteristicRead(gatt: BluetoothGatt,
                                              characteristic: BluetoothGattCharacteristic,
                                              status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    broadcastUpdate(deviceID,ACTION_DATA_AVAILABLE, characteristic, mListBleDevices.get(deviceID)!!.getCharateristicIndex(characteristic),"READ")
            //        Log.w(TAG, "onCharacteristicRead")
                }
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt,
                                                 characteristic: BluetoothGattCharacteristic) {
                broadcastUpdate(deviceID,ACTION_DATA_AVAILABLE, characteristic, mListBleDevices.get(deviceID)!!.getCharateristicIndex(characteristic),"CHANGED")
           //     Log.w(TAG, "onCharacteristicChanged")




            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
                super.onCharacteristicWrite(gatt, characteristic, status)

                broadcastUpdate(deviceID,ACTION_DATA_AVAILABLE, characteristic!!, mListBleDevices.get(deviceID)!!.getCharateristicIndex(characteristic),"WRITE")
            //    Log.w(TAG, "onCharacteristicRead")
            }

        })

        return true
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    fun disconnect(deviceID:Int) {
        if (bluetoothAdapter == null || mListBleDevices.get(deviceID)!!.bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        for(serviceIndex in 0 until  mListBleDevices.get(deviceID)!!.mListServices!!.size){
            for(characteristicIndex in 0 until mListBleDevices.get(deviceID)!!.mListCharacteristic!!.get(serviceIndex).size){
                setCharacteristicNotification(deviceID,serviceIndex,characteristicIndex,false,DISABLE_NOTIFICATION_VALUE)
            }
        }

        mListBleDevices.get(deviceID)!!.bluetoothGatt!!.disconnect()
        broadcastUpdate(deviceID,ACTION_GATT_DISCONNECTING)
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    fun close(deviceID:Int) {
        for(i in 0 until mListBleDevices.size){
            if (mListBleDevices.get(i)!!.bluetoothGatt == null) {
                return
            }else{
                mListBleDevices.get(i)!!.bluetoothGatt !!.close()
                mListBleDevices.get(i)!!.bluetoothGatt  = null
          //      mListBleDevices.remove(deviceID)
            }
        }
    }

    /**
     * Request a read on a given `BluetoothGattCharacteristic`.
     *
     * @param deviceID device index in mListBleDevices
     * @param serviceIndex The service index.
     * @param charateristicIndex The characteristic index.

     */
    fun readCharacteristic(deviceID:Int,serviceIndex:Int,charateristicIndex:Int) {
        if (bluetoothAdapter == null ||mListBleDevices.get(deviceID)!!.bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        Log.w(TAG, "TRYING to read characteristic")

        mListBleDevices.get(deviceID)!!.bluetoothGatt!!.readCharacteristic(mListBleDevices.get(deviceID)!!.getCharateristic(serviceIndex,charateristicIndex))
    }

    /**
     * Request a write on a given `BluetoothGattCharacteristic`.
     *
     * @param deviceID device index in mListBleDevices
     * @param serviceIndex  service index.
     * @param charateristicIndex  characteristic index.
     * @param data data to write.

     */

    fun writeCharacteristic(deviceID:Int,serviceIndex:Int,charateristicIndex:Int,data:String) {
        if (bluetoothAdapter == null || mListBleDevices.get(deviceID)!!.bluetoothGatt  == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        val charateristic= mListBleDevices.get(deviceID)!!.getCharateristic(serviceIndex,charateristicIndex)
        charateristic!!.value=data.toByteArray()
        mListBleDevices.get(deviceID)!!.bluetoothGatt!!.writeCharacteristic(charateristic)
    }

    /**
     * Enables or disables notification on a give characteristic.

     * @param deviceID device index in mListBleDevices
     * @param serviceIndex  service index.
     * @param charateristicIndex  characteristic index.
     * @param enabled If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(deviceID: Int,serviceIndex:Int,charateristicIndex:Int,
                                      enable: Boolean,descriptorValue:ByteArray) {
        if (bluetoothAdapter == null || mListBleDevices.get(deviceID)!!.bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }

        val charateristic= mListBleDevices.get(deviceID)!!.getCharateristic(serviceIndex,charateristicIndex)
        mListBleDevices[deviceID]!!.bluetoothGatt!!.setCharacteristicNotification(charateristic, enable)

        val descriptor = charateristic!!.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
        if(descriptor!=null){
            descriptor.value = if (enable) descriptorValue else DISABLE_NOTIFICATION_VALUE
            mListBleDevices[deviceID]!!.bluetoothGatt!!.writeDescriptor(descriptor)
        }


    }


    fun getBluetoothDevice(deviceId:Int): BleDevice{
        return mListBleDevices.get(deviceId)!!
    }

    companion object {
        val EXTRA_CHARATERISTIC_DATA = "com.example.bluetooth.le.EXTRA_CHARATERISTIC_DATA"
        val EXTRA_SERVICE_INDEX = "com.example.bluetooth.le.EXTRA_SERVICE_INDEX"
        val EXTRA_CHARATERISTIC_INDEX = "com.example.bluetooth.le.EXTRA_CHARATERISTIC_INDEX"
        val EXTRA_DEVICE_ID = "com.example.bluetooth.le.EXTRA_DEVICE_ID"
        val EXTRA_ACTION_TYPE = "com.example.bluetooth.le.EXTRA_ACTION_TYPE"
        val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
        val ACTION_GATT_CONNECTING= "com.example.bluetooth.le.ACTION_GATT_CONNECTING"
        val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
        val ACTION_GATT_DISCONNECTING= "com.example.bluetooth.le.ACTION_GATT_DISCONNECTING"
        val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
        val CLIENT_CHARACTERISTIC_CONFIG_UUID= UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    }
}
