package  com.izaphe.ble.service.healththermometer

import com.izaphe.ble.service.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class TemperatureMeasurement : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray




    override fun getValue():String {
        return "aaaaaaaaa"
      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}