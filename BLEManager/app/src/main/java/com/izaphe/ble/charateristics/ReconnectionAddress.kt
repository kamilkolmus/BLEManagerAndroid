package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class ReconnectionAddress : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray


    override fun getValueAsString(): String {

        if(packet==null){
            return "Unknown Reconnection Address"
        }
        if (packet.size<8){
            return "Unknown Reconnection Address"
        }


      return String.format("%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X", packet[7],packet[6],packet[5],packet[4],packet[3],packet[2],packet[1],packet[0])
    }

    override fun getValueAsDouble(): Double? {
        return null
    }




}