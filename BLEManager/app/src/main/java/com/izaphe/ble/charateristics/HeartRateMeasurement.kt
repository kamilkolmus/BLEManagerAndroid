package com.izaphe.ble.charateristics

import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic
import com.izaphe.ble.utils.BleUtils


class HeartRateMeasurement : AbstractBleCharacteristic() {
    override lateinit var packet: ByteArray


    override fun getValueAsString(): String {
        if(packet==null){
            return "Unknown Heart Rate"
        }
        if(packet.size<2){
            return "Unknown Heart Rate"
        }
        val sb=StringBuilder()



        val flagHeartRateValueFormat=(packet[0].toInt() and  1) > 0//boolean
        val flagSensoContactStatusbits=(packet[0].toInt() and  6) shr 1//2 bits (int value)
        val flagEnergyExpendedStatusbit=(packet[0].toInt() and  8) > 0//boolean
        val flagRR_Intervalbit=(packet[0].toInt() and  16) > 0//boolean



        if(flagHeartRateValueFormat){
            //UINT16
            if(packet.size<3){
                return "Unknown Heart Rate"
            }
            sb.append("Heart Rate: "+BleUtils.bytesToUnsignedShort(packet[1],packet[2])+" bpm")

        }else{
            //UINT8
            sb.append("Heart Rate: "+(packet[1].toInt() and 255)+" bpm")
        }

        //Hmmm........maybe later interpretation
        if(flagEnergyExpendedStatusbit){

        }else{

        }



        if(flagRR_Intervalbit){
            if(flagEnergyExpendedStatusbit){
                if(flagHeartRateValueFormat){
                    if(packet.size<7){
                        sb.append("\nUnknown RR-Interval")
                    }else{
                        sb.append("\nRR-Interval: "+ String.format("%.2f",BleUtils.bytesToUnsignedShort(packet[5],packet[6])/1024.0)+" s")
                    }

                }else{
                    if(packet.size<6){
                        sb.append("\nUnknown RR-Interval")
                    }else{
                        sb.append("\nRR-Interval: "+String.format("%.2f",BleUtils.bytesToUnsignedShort(packet[4],packet[5])/1024.0)+" s")
                    }
                }

            }else{
                if(flagHeartRateValueFormat){
                    if(packet.size<5){
                        sb.append("\nUnknown RR-Interval")
                    }else{
                        sb.append("\nRR-Interval: "+String.format("%.2f",BleUtils.bytesToUnsignedShort(packet[3],packet[4])/1024.0)+" s")
                    }
                }else{
                    if(packet.size<4){
                        sb.append("\nUnknown RR-Interval")
                    }else{
                        sb.append("\nRR-Interval: "+String.format("%.2f",BleUtils.bytesToUnsignedShort(packet[2],packet[3])/1024.0)+" s")
                    }
                }
            }
        }
        return sb.toString()
    }

    override fun getValueAsDouble(): Double? {
        if(packet==null){
            return null
        }
        if(packet.size<2){
            return null
        }



        val flagHeartRateValueFormat=(packet[0].toInt() and  1) > 0//boolean


        if(flagHeartRateValueFormat){
            //UINT16
            if(packet.size<3){
                return null
            }
           return BleUtils.bytesToUnsignedShort(packet[1],packet[2]).toDouble()

        }else{
            //UINT8
            return (packet[1].toInt() and 255).toDouble()
        }
    }


}