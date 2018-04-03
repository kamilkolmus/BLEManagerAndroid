package com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


class TemperatureType : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray
    val tempType:HashMap<Int,String> = HashMap()

    override fun getValueAsString(): String {
        if(packet==null){
            return "Unknown"
        }
        if(packet.isEmpty()){
            return "Unknown"
        }
        val type= tempType.get(packet[0].toInt()) ?: return "Unknown Temperature Type"
        return type
    }

    init {
        tempType.put(1,"Armpit")
        tempType.put(2,"Body")
        tempType.put(3,"Ear")
        tempType.put(4,"Finger")
        tempType.put(5,"Gastro-intestinal Tract")
        tempType.put(6,"Mouth")
        tempType.put(7,"Rectum")
        tempType.put(8,"Toe")
        tempType.put(9,"Tympanum")
    }

    override fun getValueAsDouble(): Double? {
        return null
    }
}