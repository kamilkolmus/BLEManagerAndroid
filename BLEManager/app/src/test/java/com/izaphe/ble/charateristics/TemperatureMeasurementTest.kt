package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleCharacteristicInitializer
import org.junit.Test

import org.junit.Assert.*

class TemperatureMeasurementTest {

    @Test
    fun getValue() {
        val charateristic= BleCharacteristicInitializer.getCharacteristicObject("00002a1c-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(0.toByte(),1.toByte(),0.toByte(),0.toByte(),0.toByte())
        assertEquals("Temperature Measurement: 1,0 C",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(0.toByte(),250.toByte(),0.toByte(),0.toByte(),0.toByte())
        assertEquals("Temperature Measurement: 250,0 C",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(1.toByte(),250.toByte(),0.toByte(),0.toByte(),0.toByte())
        assertEquals("Temperature Measurement: 250,0 F",charateristic.getValueAsString())


        charateristic!!.packet=byteArrayOf(0.toByte(),25.toByte(),0.toByte(),0.toByte(),(-1).toByte())
        assertEquals("Temperature Measurement: 2,5 C",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(0.toByte(),25.toByte(),0.toByte(),0.toByte(),(-1).toByte())
        assertEquals("Temperature Measurement: 2,5 C",charateristic.getValueAsString())


        charateristic!!.packet=byteArrayOf(6.toByte(),25.toByte(),0.toByte(),0.toByte(),(-1).toByte(),
                225.toByte(),7.toByte(),12.toByte(),30.toByte(),13.toByte(),12.toByte(),11.toByte(),
                1.toByte())
        assertEquals("Temperature Measurement: 2,5 C\n2017.12.30 13:12:11\nArmpit",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(2.toByte(),25.toByte(),0.toByte(),0.toByte(),(-1).toByte(),
                225.toByte(),7.toByte(),12.toByte(),30.toByte(),13.toByte(),12.toByte(),11.toByte())
        assertEquals("Temperature Measurement: 2,5 C\n2017.12.30 13:12:11",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(4.toByte(),25.toByte(),0.toByte(),0.toByte(),(-1).toByte(),
                1.toByte())
        assertEquals("Temperature Measurement: 2,5 C\nArmpit",charateristic.getValueAsString())


    }
}