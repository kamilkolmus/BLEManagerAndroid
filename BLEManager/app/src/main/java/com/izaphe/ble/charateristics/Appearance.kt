package  com.izaphe.ble.charateristics


import com.izaphe.ble.charateristics.abstractcharateristic.AbstractBleCharacteristic


/**
 * Created by Kamil on 2018-03-29.
 */
class Appearance : AbstractBleCharacteristic() {


    override lateinit var packet: ByteArray

    val categories:HashMap<Int,String> = HashMap()
    val subCategories:HashMap<Pair<Int,Int>,String> = HashMap()

    init {
        initialize()
    }

    override fun getValueAsString():String {
        if(packet==null){
            return "Unknown"
        }
        if (packet.size<2){
            return "Not Specified"
        }
        //(((byte2.toInt() and 255) shl 8) or (byte1.toInt() and 255))
        val category=((packet[0].toInt() and 255) shl 0) or ((packet[1].toInt() and 3)shl 8)
        val subCategory=((packet[1].toInt() and 252) shr 2)

        if(category==0){
            return "Unknown Device Category"
        }

        var sCategory=categories.get(category)
        var sSubCategory=subCategories.get(Pair(category,subCategory))

        if(sCategory==null){
            sCategory="Unknown Device Category"
        }
        if(sSubCategory==null){
            sSubCategory="Unknown Device Sub-Category"
        }

        return sCategory+"\n"+sSubCategory

    }

    override fun getValueAsDouble(): Double? {
        return null
    }

    private fun initialize() {
        categories.put(0,"Unknown")
        categories.put(1,"Phone")
        categories.put(2,"Computer")
        categories.put(3,"Watch")
        categories.put(4,"Clock")
        categories.put(5,"Display")
        categories.put(6,"Remote Control")
        categories.put(7,"Eye-glasses")
        categories.put(8,"Tag")
        categories.put(9,"Keyring")
        categories.put(10,"Media Player")
        categories.put(11,"Barcode Scanner")
        categories.put(12,"Thermometer")
        categories.put(13,"Heart rate Sensor")
        categories.put(14,"Blood Pressure")
        categories.put(15,"Human Interface Device (HID)")
        categories.put(16,"Glucose Meter")
        categories.put(17,"Running Walking Sensor")
        categories.put(18,"Cycling")
        categories.put(49,"Pulse Oximeter")
        categories.put(50,"Weight Scale")
        categories.put(51,"Personal Mobility Device")
        categories.put(52,"Continuous Glucose Monitor")
        categories.put(53,"Insulin Pump")
        categories.put(54,"Medication Delivery")
        categories.put(81,"Outdoor Sports Activity")

        subCategories.put(Pair(0,0),"Unknown")
        subCategories.put(Pair(1,0),"Generic Phone")
        subCategories.put(Pair(2,0),"Generic Computer")
        subCategories.put(Pair(3,0),"Generic Watch")
        subCategories.put(Pair(3,1),"Watch: Sports Watch")
        subCategories.put(Pair(4,0),"Generic Clock")
        subCategories.put(Pair(5,0),"Generic Display")
        subCategories.put(Pair(6,0),"Generic Remote Control")
        subCategories.put(Pair(7,0),"Generic Eye-glasses")
        subCategories.put(Pair(8,0),"Generic Tag")
        subCategories.put(Pair(9,0),"Generic Keyring")
        subCategories.put(Pair(10,0),"Generic Media Player")
        subCategories.put(Pair(11,0),"Generic Barcode Scanner")
        subCategories.put(Pair(12,0),"Generic Thermometer")
        subCategories.put(Pair(12,1),"Thermometer: Ear")
        subCategories.put(Pair(13,0),"Generic Heart rate Sensor")
        subCategories.put(Pair(13,1),"Heart Rate Sensor: Heart Rate Belt")
        subCategories.put(Pair(14,0),"Generic Blood Pressure")
        subCategories.put(Pair(14,1),"Blood Pressure: Arm")
        subCategories.put(Pair(14,2),"Blood Pressure: Wrist")
        subCategories.put(Pair(15,0),"Human Interface Device (HID)")
        subCategories.put(Pair(15,1),"Keyboard")
        subCategories.put(Pair(15,2),"Mouse")
        subCategories.put(Pair(15,3),"Joystick")
        subCategories.put(Pair(15,4),"Gamepad")
        subCategories.put(Pair(15,5),"Digitizer Tablet")
        subCategories.put(Pair(15,6),"Card Reader")
        subCategories.put(Pair(15,7),"Digital Pen")
        subCategories.put(Pair(15,8),"Barcode Scanner")
        subCategories.put(Pair(16,0),"Generic Glucose Meter")
        subCategories.put(Pair(17,0),"Generic: Running Walking Sensor")
        subCategories.put(Pair(17,1),"Running Walking Sensor: In-Shoe")
        subCategories.put(Pair(17,2),"Running Walking Sensor: On-Shoe")
        subCategories.put(Pair(17,3),"Running Walking Sensor: On-Hip")
        subCategories.put(Pair(18,0),"Generic: Cycling")
        subCategories.put(Pair(18,1),"Cycling: Cycling Computer")
        subCategories.put(Pair(18,2),"Cycling: Speed Sensor")
        subCategories.put(Pair(18,3),"Cycling: Cadence Sensor")
        subCategories.put(Pair(18,4),"Cycling: Power Sensor")
        subCategories.put(Pair(18,5),"Cycling: Speed and Cadence Sensor")
        subCategories.put(Pair(49,0),"Generic: Pulse Oximeter")
        subCategories.put(Pair(49,1),"Fingertip")
        subCategories.put(Pair(49,2),"Wrist Worn")
        subCategories.put(Pair(50,0),"Generic: Weight Scale")
        subCategories.put(Pair(51,0),"Generic Personal Mobility Device")
        subCategories.put(Pair(51,1),"Powered Wheelchair")
        subCategories.put(Pair(51,2),"Mobility Scooter")
        subCategories.put(Pair(52,0),"Generic Continuous Glucose Monitor")
        subCategories.put(Pair(53,0),"Generic Insulin Pump")
        subCategories.put(Pair(53,1),"Insulin Pump, durable pump")
        subCategories.put(Pair(53,4),"Insulin Pump, patch pump")
        subCategories.put(Pair(53,8),"Insulin Pen")
        subCategories.put(Pair(54,0),"Generic Medication Delivery")
        subCategories.put(Pair(55,0),"Generic: Outdoor Sports Activity")
        subCategories.put(Pair(55,1),"Location Display Device")
        subCategories.put(Pair(55,2),"Location and Navigation Display Device")
        subCategories.put(Pair(55,3),"Location Pod")
        subCategories.put(Pair(55,4),"Location and Navigation Pod")
    }


}