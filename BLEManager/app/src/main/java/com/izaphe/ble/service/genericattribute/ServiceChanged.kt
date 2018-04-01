package com.izaphe.ble.service.genericattribute

import com.izaphe.ble.service.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils
import unsigned.toUshort
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Created by Kamil on 2018-03-29.
 */
class ServiceChanged : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray




    override fun getValue():String {


        if(packet.size<4){
            return "Not specified"
        }

        val StartOfAffectedAttributeHandleRange=BleUtils.bytesToUnsignedShort(packet[0],packet[1])
        val StopOfAffectedAttributeHandleRange=BleUtils.bytesToUnsignedShort(packet[2],packet[3])
        if(StartOfAffectedAttributeHandleRange<1 || StopOfAffectedAttributeHandleRange<1){
            return "Not specified"
        }
        return "Start of Affected Attribute Handle Range ="+StartOfAffectedAttributeHandleRange+", Stop of Affected Attribute Handle Range ="+StopOfAffectedAttributeHandleRange

    }




}