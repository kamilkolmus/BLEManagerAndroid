package  com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Created by Kamil on 2018-03-29.
 */
class IntermediateTemperature : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray




    override fun getValueAsString():String {

        if(packet==null){
            return "Unknown Temperature Measurement"
        }
        if(packet.size<5){
            return "Unknown Temperature Measurement"
        }

        val sb=StringBuilder()

        val flagTempInFahr=(packet[0].toInt() and  1) > 0
        val flagtimeStamp=(packet[0].toInt() and  2) > 0
        val flagTempType=(packet[0].toInt() and  4) > 0




        val mantissa= ByteBuffer.wrap(byteArrayOf(packet[1],packet[2],packet[3],0)).order(ByteOrder.LITTLE_ENDIAN).getInt();
        val exponent=packet[4].toDouble()
        val temp= String.format("%.1f",mantissa*Math.pow(10.0,exponent))


        if (flagTempInFahr){
            sb.append("Temperature Measurement: "+ temp+" F")
        }else{
            sb.append("Temperature Measurement: "+ temp+" C")
        }
        if(flagtimeStamp){
            if(packet.size<12){
                sb.append("\nUnknown Data Format")
            }else {
                val timeStampCharacteristic = DateTime()
                timeStampCharacteristic.packet = byteArrayOf(packet[5], packet[6], packet[7], packet[8], packet[9], packet[10], packet[11])
                sb.append("\n${timeStampCharacteristic.getValueAsString()}")
            }

        }
        if(flagTempType){
            if(flagtimeStamp){
                if(packet.size<13){
                    sb.append("\nUnknown Temperature Type")
                }else{
                    val tempTypeCharacteristic = TemperatureType()
                    tempTypeCharacteristic.packet= byteArrayOf(packet[12])
                    sb.append("\n${tempTypeCharacteristic.getValueAsString()}")
                }

            }else{
                if(packet.size<6){
                    sb.append("\nUnknown Temperature Type")
                }else{
                    val tempTypeCharacteristic = TemperatureType()
                    tempTypeCharacteristic.packet= byteArrayOf(packet[5])
                    sb.append("\n${tempTypeCharacteristic.getValueAsString()}")
                }

            }

        }

        return sb.toString()


    }

    override fun getValueAsDouble(): Double? {

        if(packet==null){
            return null
        }
        if(packet.size<5){
            return null
        }

        val mantissa= ByteBuffer.wrap(byteArrayOf(packet[1],packet[2],packet[3],0)).order(ByteOrder.LITTLE_ENDIAN).getInt();
        val exponent=packet[4].toDouble()
        return BleUtils.round(mantissa*Math.pow(10.0,exponent),1)

    }
}