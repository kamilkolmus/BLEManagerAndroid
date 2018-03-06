package com.i7xaphe.blemanager

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.*

/**
 * Created by Kamil on 2018-03-04.
 */
object BLEConverter {

    fun getServiceName(uuid: String): String {
        println(uuid)
        return when (uuid) {
            "00001800-0000-1000-8000-00805f9b34fb" -> "Generic Access"
            "00001801-0000-1000-8000-00805f9b34fb" -> "Generic Attribute"
            else -> "Custom Service"

        }
    }

    fun getCharateristicName(uuid: String): String {
        println(uuid)
        return when (uuid) {
            "00002a00-0000-1000-8000-00805f9b34fb" -> "Device Name"
            "00002a01-0000-1000-8000-00805f9b34fb" -> "Appearance"
            "00002a04-0000-1000-8000-00805f9b34fb" -> "Peripheral Preferred Connection Parameters"
            "00002a06-0000-1000-8000-00805f9b34fb" -> "Central Address Resolution"
            "00002a05-0000-1000-8000-00805f9b34fb" -> "Service Changed"
            else -> "Custom Characteristic"

        }
    }

    fun UIIDFilter(uuid: String): String {

        return when (uuid.substring(0, 4) + uuid.substring(8)) {
            "0000-0000-1000-8000-00805f9b34fb" -> uuid.substring(4, 8).toUpperCase()
            else -> uuid.toUpperCase()

        }
    }


    fun getProperties(properties: Int): String {

        var s = " "
        println("stert.")
        if (PROPERTY_BROADCAST and properties != 0)
            s += ("BROADCAST ")

        if (PROPERTY_READ and properties != 0)

            s += "READ "

        if (PROPERTY_WRITE_NO_RESPONSE and properties != 0)

            s += ("WRITE NO RESPONSE ")

        if (PROPERTY_WRITE and properties != 0)
            s += ("WRITE ")

        if (PROPERTY_NOTIFY and properties != 0)
            s.plus("NOTIFY ")

        if (PROPERTY_INDICATE and properties != 0)
            s += ("INDICATE ")

        if (PROPERTY_SIGNED_WRITE and properties != 0)
            s.plus("SIGNED_WRITE ")

        if (PROPERTY_EXTENDED_PROPS and properties != 0)

            s += ("EXTENDED_PROPS ")


        return s

    }

}