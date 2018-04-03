package com.izaphe.ble.charateristics

import com.izaphe.ble.utils.BleCharacteristicInitializer
import org.junit.Test

import org.junit.Assert.*

class ServiceChangedTest {

    @Test
    fun getValue() {
        //search for ServiceChanged characteristic
        val charateristic= BleCharacteristicInitializer.getCharacteristicObject("00002a05-0000-1000-8000-00805f9b34fb")
        assertNotNull(charateristic)
        charateristic!!.packet=byteArrayOf(255.toByte(),255.toByte(),0,0)

        assertEquals("Start of Affected Attribute Handle Range ="+65535+", Stop of Affected Attribute Handle Range ="+0,
                charateristic.getValueAsString())

        charateristic!!.packet=byteArrayOf(1,0,2,0)
        assertEquals("Start of Affected Attribute Handle Range ="+1+", Stop of Affected Attribute Handle Range ="+2,
                charateristic.getValueAsString())


    }
}