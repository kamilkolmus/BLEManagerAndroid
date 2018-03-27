package com.i7xaphe.blemanager

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by Kamil on 2018-03-26.
 */
object Utils {

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



    fun getPropertiesTextViews(properties: Int, context: Context): ArrayList<TextView> {


        val list: ArrayList<TextView> = ArrayList()
        var lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        lparams.setMargins(12,0,0,0)



        if (BluetoothGattCharacteristic.PROPERTY_BROADCAST and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "BROADCAST"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }
        if (BluetoothGattCharacteristic.PROPERTY_READ and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "READ"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }
        if (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE and properties != 0) {

            list.add(TextView(context))
            list.last().text = "WRITE NO RESPONSE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }
        if (BluetoothGattCharacteristic.PROPERTY_WRITE and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "WRITE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true

        }
        if (BluetoothGattCharacteristic.PROPERTY_NOTIFY and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "NOTIFY"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true

        }
        if (BluetoothGattCharacteristic.PROPERTY_INDICATE and properties != 0) {

            list.add(TextView(context))
            list.last().text = "INDICATE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }
        if (BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "SIGNED WRITE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }
        if (BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS and properties != 0) {

            list.add(TextView(context))
            list.last().text  = "EXTENDED PROPS"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
        }

        return list

    }


    fun getProperties(properties: Int): String {

        var s = " "
        println("stert.")
        if (BluetoothGattCharacteristic.PROPERTY_BROADCAST and properties != 0)
            s += ("BROADCAST ")

        if (BluetoothGattCharacteristic.PROPERTY_READ and properties != 0)

            s += "READ "

        if (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE and properties != 0)

            s += ("WRITE NO RESPONSE ")

        if (BluetoothGattCharacteristic.PROPERTY_WRITE and properties != 0)
            s += ("WRITE ")

        if (BluetoothGattCharacteristic.PROPERTY_NOTIFY and properties != 0)
            s+=("NOTIFY ")

        if (BluetoothGattCharacteristic.PROPERTY_INDICATE and properties != 0)
            s += ("INDICATE ")

        if (BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE and properties != 0)
            s+=("SIGNED_WRITE ")

        if (BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS and properties != 0)

            s += ("EXTENDED_PROPS ")


        return s

    }

}