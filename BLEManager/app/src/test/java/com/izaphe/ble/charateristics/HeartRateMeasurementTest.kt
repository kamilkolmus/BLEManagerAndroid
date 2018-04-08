package com.izaphe.ble.charateristics


import com.izaphe.ble.utils.BleUtils
import org.junit.Test

import org.junit.Assert.*

class HeartRateMeasurementTest {

    @Test
    fun getValue() {
        val charateristic= BleUtils.getCharacteristicObject("00002a37-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(0.toByte(),60.toByte())
        assertEquals("Heart Rate: 60 bpm",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(1.toByte(),60.toByte(),0.toByte())
        assertEquals("Heart Rate: 60 bpm",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(1.toByte(),60.toByte(),1.toByte())
        assertEquals("Heart Rate: 316 bpm",charateristic.getValueAsString())

        //this  can be change later
        charateristic!!.packet=byteArrayOf(3.toByte(),60.toByte(),1.toByte(),0,0)
        assertEquals("Heart Rate: 316 bpm",charateristic.getValueAsString())


        charateristic!!.packet=byteArrayOf(17.toByte(),60.toByte(),1.toByte(),128.toByte(),4)
        assertEquals("Heart Rate: 316 bpm\nRR-Interval: 1,13 s",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(25.toByte(),60.toByte(),1.toByte(),0,0,0,4)
        assertEquals("Heart Rate: 316 bpm\nRR-Interval: 1,00 s",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(25.toByte(),60.toByte(),1.toByte(),0,0,0,4)
        assertEquals("Heart Rate: 316 bpm\nRR-Interval: 1,00 s",charateristic.getValueAsString())

    }
}