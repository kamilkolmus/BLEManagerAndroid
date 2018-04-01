package com.izaphe.ble.service.heartrate

import com.izaphe.ble.service.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class HeartRateMeasurement : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray

    override fun getValue(): String {
        return "aaaaaaaaa"
    }



}
