package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleCharacteristicInitializer
import org.junit.Test

import org.junit.Assert.*

class ReconnectionAddressTest {

    @Test
    fun getValue() {

        val charateristic= BleCharacteristicInitializer.getCharacteristicObject("00002a03-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(10.toByte(),7.toByte(),6.toByte(),5.toByte(),4.toByte(),3.toByte(),254.toByte(),255.toByte())

        assertEquals("FF:FE:03:04:05:06:07:0A",charateristic.getValueAsString())
    }
}