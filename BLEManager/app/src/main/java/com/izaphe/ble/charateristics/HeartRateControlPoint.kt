package com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


class HeartRateControlPoint : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray


    override fun getValueAsString(): String {
        if(packet==null){
            return "Unknown"
        }
        if(packet.isEmpty()){
            return "Unknown"
        }
        //Reset Energy Expended: resets the value of the Energy Expended field in the Heart Rate Measurement characteristic to 0
        if(packet[0].toInt()==1){
            return "Reset"
        }

        return "NoAction"
    }
    override fun getValueAsDouble(): Double? {
        return null
    }

}