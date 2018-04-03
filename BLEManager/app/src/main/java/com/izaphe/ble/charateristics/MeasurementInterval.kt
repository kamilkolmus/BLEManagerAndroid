package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils


/**
 * Created by Kamil on 2018-03-29.
 */
class MeasurementInterval : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray




    override fun getValueAsString():String {

        if(packet==null){
            return "Unknown"
        }
        if(packet.size<2){
            return "Unknown Measurement Interval"
        }

        val interval=BleUtils.bytesToUnsignedShort(packet[0],packet[1])
        if(interval<1) {
            return "Unknown Measurement Interval"
        }
        return "Measurement Interval: $interval s"

    }

    override fun getValueAsDouble(): Double? {
        if(packet==null){
            return null
        }
        if(packet.size<2){
            return null
        }

        return BleUtils.bytesToUnsignedShort(packet[0],packet[1]).toDouble()
    }
}