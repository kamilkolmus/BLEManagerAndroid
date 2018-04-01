package  com.izaphe.ble.service.genericaccess

import com.izaphe.ble.service.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils


/**
 * Created by Kamil on 2018-03-29.
 */
class PeripheralPreferredConnectionParameters : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray


    override fun getValue(): String {
        if(packet==null){
             return "Unknown"
        }
        if (packet.size<8){
            return "Not specified"
        }
        val minConnectionInterval= BleUtils.bytesToUnsignedShort(packet[0],packet[1])
        val maxConnectionInterval= BleUtils.bytesToUnsignedShort(packet[2],packet[3])
        val slaveLatency= BleUtils.bytesToUnsignedShort(packet[4],packet[5])
        val ConnectionSupervisionTimeoutMultiplier= BleUtils.bytesToUnsignedShort(packet[6],packet[7])
        if(minConnectionInterval*1.25<6 || minConnectionInterval*1.25>3200){
            return "Not specified"
        }
        if(maxConnectionInterval*1.25<6 || maxConnectionInterval*1.25>3200){
            return "Not specified"
        }
        if(slaveLatency<0 || slaveLatency>1000){
            return "Not specified"
        }
        if(ConnectionSupervisionTimeoutMultiplier<10 || ConnectionSupervisionTimeoutMultiplier>3200){
            return "Not specified"
        }

        return  "Minimum Connection Interval: "+minConnectionInterval*1.25+"ms\n"+
                "Maximum Connection Interval: "+maxConnectionInterval*1.25+"ms\n"+
                "Slave Latency: "+slaveLatency+"ms\n"+
                "Connection Supervision Timeout Multiplier: "+ConnectionSupervisionTimeoutMultiplier+"ms\n"
    }



}