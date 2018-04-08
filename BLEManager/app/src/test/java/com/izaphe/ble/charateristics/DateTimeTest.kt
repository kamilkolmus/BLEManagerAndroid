package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleUtils
import org.junit.Test

import org.junit.Assert.*

class DateTimeTest {

    @Test
    fun getValue() {
        val charateristic= BleUtils.getCharacteristicObject("00002a08-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(226.toByte(),7.toByte(),1.toByte(),1.toByte(),1.toByte(),1.toByte(),1.toByte())
        assertEquals("2018.01.01 01:01:01",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(225.toByte(),7.toByte(),12.toByte(),30.toByte(),13.toByte(),12.toByte(),11.toByte())
        assertEquals("2017.12.30 13:12:11",charateristic.getValueAsString())


    }
}