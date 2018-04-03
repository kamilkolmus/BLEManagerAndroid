package com.izaphe.ble.charateristics.abstractcharateristic

/**
 * Created by Kamil on 2018-03-29.
 */
abstract class AbstractBleCharacteristic {
     abstract var packet:ByteArray
     abstract fun getValueAsString():String
     abstract fun getValueAsDouble():Double?

}