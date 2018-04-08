package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleUtils
import org.junit.Test

import org.junit.Assert.*

class AppearanceTest {

    @Test
    fun getValue() {
        val charateristic= BleUtils.getCharacteristicObject("00002a01-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(0.toByte(),0.toByte())
        assertEquals("Unknown device category",charateristic.getValueAsString())


        charateristic!!.packet=byteArrayOf(1.toByte(),0.toByte())
        assertEquals("Phone\nGeneric Phone",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(53.toByte(),(8 shl 2).toByte())
        assertEquals("Insulin Pump\nInsulin Pen",charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(53.toByte(),(9 shl 2).toByte())
        assertEquals("Insulin Pump\nUnknown device sub-category",charateristic.getValueAsString())


    }
}