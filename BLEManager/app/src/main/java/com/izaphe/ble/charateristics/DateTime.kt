package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils


/**
 * Created by Kamil on 2018-03-29.
 */
class DateTime : AbstractBleCharacteristic() {
    override  lateinit var packet: ByteArray


    override fun getValueAsString(): String {
        if(packet==null){
            return "Unknown"
        }
        if (packet.size<7){
            return "Not Specified"
        }

        val year=BleUtils.bytesToUnsignedShort(packet[0],packet[1])
        val mounth=packet[2].toShort()
        val day=packet[3].toShort()
        val hours=packet[4].toShort()
        val minutes=packet[5].toShort()
        val seconds=packet[6].toShort()
        val sb=StringBuilder()
        sb.append("$year")
        sb.append(".")
        sb.append(String.format("%02d", mounth))
        sb.append(".")
        sb.append(String.format("%02d", day))
        sb.append(" ")
        sb.append(String.format("%02d", hours))
        sb.append(":")
        sb.append(String.format("%02d", minutes))
        sb.append(":")
        sb.append(String.format("%02d", seconds))

      return sb.toString()
    }

    override fun getValueAsDouble(): Double? {
        return null
    }

}