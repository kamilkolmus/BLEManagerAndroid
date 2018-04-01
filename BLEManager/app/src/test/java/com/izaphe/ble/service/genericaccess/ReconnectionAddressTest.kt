package com.izaphe.ble.service.genericaccess

import com.izaphe.ble.utils.BleCharacteristicInitializer
import org.junit.Test

import org.junit.Assert.*

class ReconnectionAddressTest {

    @Test
    fun getValue() {

        val charateristic= BleCharacteristicInitializer.getCharacteristicObject("00002a03-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)

        charateristic!!.packet=byteArrayOf(255.toByte(),254.toByte(),3.toByte(),4.toByte(),5.toByte(),6.toByte(),7.toByte(),10.toByte())

        assertEquals("FF:FE:03:04:05:06:07:0A",charateristic.getValue())
    }
}