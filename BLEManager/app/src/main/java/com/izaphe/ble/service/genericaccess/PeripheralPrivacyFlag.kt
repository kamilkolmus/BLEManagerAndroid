package  com.izaphe.ble.service.genericaccess

import com.izaphe.ble.service.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class PeripheralPrivacyFlag : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray

    override fun getValue(): String {

        if(packet==null){
            return "Unknown"
        }
        if (packet.isEmpty()){
            return "Not specified"
        }

        if(0 == packet[0].toInt()){
            return "privacy is disabled in this device"
        }
        return " privacy is enabled in this device"

    }





}