package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class PeripheralPrivacyFlag : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray

    override fun getValueAsString(): String {

        if(packet==null){
            return "Unknown"
        }
        if (packet.isEmpty()){
            return "Not Specified"
        }

        if(0 == packet[0].toInt()){
            return "privacy is disabled in this device"
        }
        return " privacy is enabled in this device"

    }

    override fun getValueAsDouble(): Double? {
        if(packet==null){
            return null
        }
        if (packet.isEmpty()){
            return null
        }

        return packet[0].toDouble()
    }




}