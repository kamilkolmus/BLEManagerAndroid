package com.izaphe.blemanager.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile.STATE_DISCONNECTED
import android.location.Address

/**
 * Created by Kamil on 2018-03-13.
 */
class BleDevice (deviceID:Int,device :BluetoothDevice) {


    var device=device
    //initial state
    //var connectionState = STATE_DISCONNECTED

    var bluetoothGatt: BluetoothGatt? = null
    //mLeServices holds BleService
    var mListServices: List<BluetoothGattService>?=null

    //mLeServices holds BleCharacteristic
    var mListCharacteristic: ArrayList<List<BluetoothGattCharacteristic>> = ArrayList()

    //return characteristic index
    fun getCharateristicIndex(characteristic: BluetoothGattCharacteristic): Pair<Int, Int> {
        var service=characteristic.service
        var serviceIndex=0;
        var charateristicIndex=0
        for (i in 0 until mListServices!!.size){
            if(service== mListServices!!.get(i)){
                serviceIndex=i
                break
            }
        }
        for (i in 0 until mListCharacteristic!!.get(serviceIndex).size){
            if(characteristic== mListCharacteristic!!.get(serviceIndex).get(i)){
                charateristicIndex=i
                break
            }
        }


        return Pair(serviceIndex,charateristicIndex)
    }
    //return characteristic
    fun getCharateristic(serviceIndex: Int, charateristicIndex: Int): BluetoothGattCharacteristic? {
        return mListCharacteristic!!.get(serviceIndex).get(charateristicIndex)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //update mListServices and mListCharacteristic
    fun updateSetvices() {
        mListServices=bluetoothGatt!!.services
        for(service in mListServices!!){
            mListCharacteristic.add(service.characteristics)
        }
    }


}