package com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils


/**
 * Created by Kamil on 2018-03-29.
 */
class ServiceChanged : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray




    override fun getValueAsString():String {

        if(packet==null){
            return "Unknown"
        }
        if(packet.size<4){
            return "Not Specified"
        }

        val StartOfAffectedAttributeHandleRange=BleUtils.bytesToUnsignedShort(packet[0],packet[1])
        val StopOfAffectedAttributeHandleRange=BleUtils.bytesToUnsignedShort(packet[2],packet[3])
        if(StartOfAffectedAttributeHandleRange<1 || StopOfAffectedAttributeHandleRange<1){
            return "Not specified"
        }
        return "Start of Affected Attribute Handle Range ="+StartOfAffectedAttributeHandleRange+", Stop of Affected Attribute Handle Range ="+StopOfAffectedAttributeHandleRange

    }

    override fun getValueAsDouble(): Double? {
       return null
    }




}