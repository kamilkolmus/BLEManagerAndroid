package  com.izaphe.ble.service.genericaccess

import com.izaphe.ble.service.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class DeviceName : AbstractBleCharacteristic() {
    override  lateinit var packet: ByteArray


    override fun getValue(): String {
      return String(packet)
    }

}