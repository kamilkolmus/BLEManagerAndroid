package com.izaphe.ble.service

/**
 * Created by Kamil on 2018-03-29.
 */
abstract class AbstractBleCharacteristic {
     abstract var packet:ByteArray
     abstract fun getValue():String?

}