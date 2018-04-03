package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class DeviceName : AbstractBleCharacteristic() {
    override  lateinit var packet: ByteArray


    override fun getValueAsString(): String {
      return String(packet)
    }

    override fun getValueAsDouble(): Double? {
        return null
    }

}