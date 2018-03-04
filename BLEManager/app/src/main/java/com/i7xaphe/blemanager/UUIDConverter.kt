package com.i7xaphe.blemanager

/**
 * Created by Kamil on 2018-03-04.
 */
 class  UUIDConverter {

    fun getServiceName(uuid:String):String{
        println(uuid)
        return when(uuid){
            "00001800-0000-1000-8000-00805f9b34fb" -> "Generic Access"
            "00001801-0000-1000-8000-00805f9b34fb" -> "Generic Attribute"
            else -> "Custom Service"

        }
    }
    fun getCharateristicName(uuid:String):String{
        println(uuid)
        return when(uuid){
            "00002a00-0000-1000-8000-00805f9b34fb" -> "DeviceName"
            "00002a01-0000-1000-8000-00805f9b34fb" -> "Appearance"
            "00002a04-0000-1000-8000-00805f9b34fb" -> "Peripheral Preferred Connection Parameters"
            "00002a06-0000-1000-8000-00805f9b34fb" -> "Central Address Resolution"
            "00002a05-0000-1000-8000-00805f9b34fb" -> "Service Changed"
            else -> "Custom Characteristic"

        }
    }
}