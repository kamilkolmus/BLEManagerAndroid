package  com.izaphe.ble.service.genericaccess

import com.izaphe.ble.service.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class ReconnectionAddress : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray


    override fun getValue(): String {

        if(packet==null){
            return "Unknown"
        }
        if (packet.size<8){
            return "Not specified"
        }


      return String.format("%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X", packet[0],packet[1],packet[2],packet[3],packet[4],packet[5],packet[6],packet[7])
    }




}