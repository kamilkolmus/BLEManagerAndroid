package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleCharacteristicInitializer
import org.junit.Test

import org.junit.Assert.*

class PeripheralPreferredConnectionParametersTest {

    @Test
    fun getValue() {

        val charateristic= BleCharacteristicInitializer.getCharacteristicObject("00002a04-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)
        charateristic!!.packet=byteArrayOf(6.toByte(),0.toByte(),6.toByte(),0.toByte(),6.toByte(),0.toByte(),10.toByte(),0.toByte())

        assertEquals( "Minimum Connection Interval: "+6*1.25+"ms\n"+
                "Maximum Connection Interval: "+6*1.25+"ms\n"+
                "Slave Latency: "+6+"ms\n"+
                "Connection Supervision Timeout Multiplier: "+10+"ms\n",
                charateristic.getValueAsString())

    }
}