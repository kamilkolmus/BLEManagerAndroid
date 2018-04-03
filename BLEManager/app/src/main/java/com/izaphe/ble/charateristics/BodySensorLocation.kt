package com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


class BodySensorLocation : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray
    val bodySensorLocation:HashMap<Int,String> = HashMap()

    override fun getValueAsString(): String {
        if(packet==null){
            return "Unknown Sensor Location"
        }
        if(packet.isEmpty()){
            return "Unknown Sensor Location"
        }
        val type= bodySensorLocation.get(packet[0].toInt()) ?: return "Unknown Sensor Location"
        return type
    }
    override fun getValueAsDouble(): Double? {
        return null
    }

    init {
        bodySensorLocation.put(0,"Other")
        bodySensorLocation.put(1,"Chest")
        bodySensorLocation.put(2,"Wrist")
        bodySensorLocation.put(3,"Finger")
        bodySensorLocation.put(4,"Hand")
        bodySensorLocation.put(5,"Ear Lobe")
        bodySensorLocation.put(6,"Foot")

    }
}