package com.izaphe.ble.utils

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import com.izaphe.blemanager.views.TouchableTextView


/**
 * Created by Kamil on 2018-03-26.
 */
object BleUtils {

    fun getServiceName(uuid: String): String {
        println(uuid)
        return when (uuid) {
            "00001800-0000-1000-8000-00805f9b34fb" -> "Generic Access"
            "00001801-0000-1000-8000-00805f9b34fb" -> "Generic Attribute"
            "00001802-0000-1000-8000-00805f9b34fb" -> "Immediate Alert"
            "00001803-0000-1000-8000-00805f9b34fb" -> "Link Loss"
            "00001804-0000-1000-8000-00805f9b34fb" -> "Tx Power"
            "00001805-0000-1000-8000-00805f9b34fb" -> "Current Time Service"
            "00001806-0000-1000-8000-00805f9b34fb" -> "Reference Time Update Service"
            "00001807-0000-1000-8000-00805f9b34fb" -> "Next DST Change Service"
            "00001808-0000-1000-8000-00805f9b34fb" -> "Glucose"
            "00001809-0000-1000-8000-00805f9b34fb" -> "Health Thermometer"
            "0000180a-0000-1000-8000-00805f9b34fb" -> "Device Information"
            "0000180d-0000-1000-8000-00805f9b34fb" -> "Heart Rate"
            "0000180e-0000-1000-8000-00805f9b34fb" -> "Phone Alert Status Service"
            "0000180f-0000-1000-8000-00805f9b34fb" -> "Battery Service"
            "00001810-0000-1000-8000-00805f9b34fb" -> "Blood Pressure"
            "00001811-0000-1000-8000-00805f9b34fb" -> "Alert Notification Service"
            "00001812-0000-1000-8000-00805f9b34fb" -> "Human Interface Device"
            "00001813-0000-1000-8000-00805f9b34fb" -> "Scan Parameters"
            "00001814-0000-1000-8000-00805f9b34fb" -> "Running Speed and Cadence"
            "00001815-0000-1000-8000-00805f9b34fb" -> "Automation IO"
            "00001816-0000-1000-8000-00805f9b34fb" -> "Cycling Speed and Cadence"
            "00001818-0000-1000-8000-00805f9b34fb" -> "Cycling Power"
            "00001819-0000-1000-8000-00805f9b34fb" -> "Location and Navigation"
            "0000181a-0000-1000-8000-00805f9b34fb" -> "Environmental Sensing"
            "0000181b-0000-1000-8000-00805f9b34fb" -> "Body Composition"
            "0000181c-0000-1000-8000-00805f9b34fb" -> "User Data"
            "0000181d-0000-1000-8000-00805f9b34fb" -> "Weight Scale"
            "0000181e-0000-1000-8000-00805f9b34fb" -> "Bond Management Service"
            "0000181f-0000-1000-8000-00805f9b34fb" -> "Continuous Glucose Monitoring"
            "00001820-0000-1000-8000-00805f9b34fb" -> "Internet Protocol Support Service"
            "00001821-0000-1000-8000-00805f9b34fb" -> "Indoor Positioning"
            "00001822-0000-1000-8000-00805f9b34fb" -> "Pulse Oximeter Service"
            "00001823-0000-1000-8000-00805f9b34fb" -> "HTTP Proxy"
            "00001824-0000-1000-8000-00805f9b34fb" -> "Transport Discovery"
            "00001825-0000-1000-8000-00805f9b34fb" -> "Object Transfer Service"
            "00001826-0000-1000-8000-00805f9b34fb" -> "Fitness Machine"
            "00001827-0000-1000-8000-00805f9b34fb" -> "Mesh Provisioning Service"
            "00001828-0000-1000-8000-00805f9b34fb" -> "Mesh Proxy Service"
            "00001829-0000-1000-8000-00805f9b34fb" -> "Reconnection Configuration"
            else -> "Custom Service"

        }

    }

    fun getCharateristicName(uuid: String): String {
        println(uuid)
        return when (uuid) {
            "00002a00-0000-1000-8000-00805f9b34fb" -> "Device Name"
            "00002a01-0000-1000-8000-00805f9b34fb" -> "Appearance"
            "00002a02-0000-1000-8000-00805f9b34fb" -> "Peripheral Privacy Flag"
            "00002a03-0000-1000-8000-00805f9b34fb" -> "Reconnection Address"
            "00002a04-0000-1000-8000-00805f9b34fb" -> "Peripheral Preferred Connection Parameters"
            "00002a05-0000-1000-8000-00805f9b34fb" -> "Service Changed"
            "00002a06-0000-1000-8000-00805f9b34fb" -> "Alert Level"
            "00002a07-0000-1000-8000-00805f9b34fb" -> "Tx Power Level"
            "00002a08-0000-1000-8000-00805f9b34fb" -> "Date Time"
            "00002a09-0000-1000-8000-00805f9b34fb" -> "Day of Week"
            "00002a0a-0000-1000-8000-00805f9b34fb" -> "Day Date Time"
            "00002a0b-0000-1000-8000-00805f9b34fb" -> "Exact Time 100"
            "00002a0c-0000-1000-8000-00805f9b34fb" -> "Exact Time 256"
            "00002a0d-0000-1000-8000-00805f9b34fb" -> "DST Offset"
            "00002a0e-0000-1000-8000-00805f9b34fb" -> "Time Zone"
            "00002a0f-0000-1000-8000-00805f9b34fb" -> "Local Time Information"
            "00002a10-0000-1000-8000-00805f9b34fb" -> "Secondary Time Zone"
            "00002a11-0000-1000-8000-00805f9b34fb" -> "Time with DST"
            "00002a12-0000-1000-8000-00805f9b34fb" -> "Time Accuracy"
            "00002a13-0000-1000-8000-00805f9b34fb" -> "Time Source"
            "00002a14-0000-1000-8000-00805f9b34fb" -> "Reference Time Information"
            "00002a15-0000-1000-8000-00805f9b34fb" -> "Time Broadcast"
            "00002a16-0000-1000-8000-00805f9b34fb" -> "Time Update Control Point"
            "00002a17-0000-1000-8000-00805f9b34fb" -> "Time Update State"
            "00002a18-0000-1000-8000-00805f9b34fb" -> "Glucose Measurement"
            "00002a19-0000-1000-8000-00805f9b34fb" -> "Battery Level"
            "00002a1a-0000-1000-8000-00805f9b34fb" -> "Battery Power State"
            "00002a1b-0000-1000-8000-00805f9b34fb" -> "Battery Level State"
            "00002a1c-0000-1000-8000-00805f9b34fb" -> "Temperature Measurement"
            "00002a1d-0000-1000-8000-00805f9b34fb" -> "Temperature Type"
            "00002a1e-0000-1000-8000-00805f9b34fb" -> "Intermediate Temperature"
            "00002a1f-0000-1000-8000-00805f9b34fb" -> "Temperature Celsius"
            "00002a20-0000-1000-8000-00805f9b34fb" -> "Temperature Fahrenheit"
            "00002a21-0000-1000-8000-00805f9b34fb" -> "Measurement Interval"
            "00002a22-0000-1000-8000-00805f9b34fb" -> "Boot Keyboard Input Report"
            "00002a23-0000-1000-8000-00805f9b34fb" -> "System ID"
            "00002a24-0000-1000-8000-00805f9b34fb" -> "Model Number String"
            "00002a25-0000-1000-8000-00805f9b34fb" -> "Serial Number String"
            "00002a26-0000-1000-8000-00805f9b34fb" -> "Firmware Revision String"
            "00002a27-0000-1000-8000-00805f9b34fb" -> "Hardware Revision String"
            "00002a28-0000-1000-8000-00805f9b34fb" -> "Software Revision String"
            "00002a29-0000-1000-8000-00805f9b34fb" -> "Manufacturer Name String"
            "00002a2a-0000-1000-8000-00805f9b34fb" -> "IEEE 11073-20601 Regulatory Certification Data List"
            "00002a2b-0000-1000-8000-00805f9b34fb" -> "Current Time"
            "00002a2c-0000-1000-8000-00805f9b34fb" -> "Magnetic Declination"
            "00002a2f-0000-1000-8000-00805f9b34fb" -> "Position 2D"
            "00002a30-0000-1000-8000-00805f9b34fb" -> "Position 3D"
            "00002a31-0000-1000-8000-00805f9b34fb" -> "Scan Refresh"
            "00002a32-0000-1000-8000-00805f9b34fb" -> "Boot Keyboard Output Report"
            "00002a33-0000-1000-8000-00805f9b34fb" -> "Boot Mouse Input Report"
            "00002a34-0000-1000-8000-00805f9b34fb" -> "Glucose Measurement Context"
            "00002a35-0000-1000-8000-00805f9b34fb" -> "Blood Pressure Measurement"
            "00002a36-0000-1000-8000-00805f9b34fb" -> "Intermediate Cuff Pressure"
            "00002a37-0000-1000-8000-00805f9b34fb" -> "Heart Rate Measurement"
            "00002a38-0000-1000-8000-00805f9b34fb" -> "Body Sensor Location"
            "00002a39-0000-1000-8000-00805f9b34fb" -> "Heart Rate Control Point"
            "00002a3a-0000-1000-8000-00805f9b34fb" -> "Removable"
            "00002a3b-0000-1000-8000-00805f9b34fb" -> "Service Required"
            "00002a3c-0000-1000-8000-00805f9b34fb" -> "Scientific Temperature Celsius"
            "00002a3d-0000-1000-8000-00805f9b34fb" -> "String"
            "00002a3e-0000-1000-8000-00805f9b34fb" -> "Network Availability"
            "00002a3f-0000-1000-8000-00805f9b34fb" -> "Alert Status"
            "00002a40-0000-1000-8000-00805f9b34fb" -> "Ringer Control point"
            "00002a41-0000-1000-8000-00805f9b34fb" -> "Ringer Setting"
            "00002a42-0000-1000-8000-00805f9b34fb" -> "Alert Category ID Bit Mask"
            "00002a43-0000-1000-8000-00805f9b34fb" -> "Alert Category ID"
            "00002a44-0000-1000-8000-00805f9b34fb" -> "Alert Notification Control Point"
            "00002a45-0000-1000-8000-00805f9b34fb" -> "Unread Alert Status"
            "00002a46-0000-1000-8000-00805f9b34fb" -> "New Alert"
            "00002a47-0000-1000-8000-00805f9b34fb" -> "Supported New Alert Category"
            "00002a48-0000-1000-8000-00805f9b34fb" -> "Supported Unread Alert Category"
            "00002a49-0000-1000-8000-00805f9b34fb" -> "Blood Pressure Feature"
            "00002a4a-0000-1000-8000-00805f9b34fb" -> "HID Information"
            "00002a4b-0000-1000-8000-00805f9b34fb" -> "Report Map"
            "00002a4c-0000-1000-8000-00805f9b34fb" -> "HID Control Point"
            "00002a4d-0000-1000-8000-00805f9b34fb" -> "Report"
            "00002a4e-0000-1000-8000-00805f9b34fb" -> "Protocol Mode"
            "00002a4f-0000-1000-8000-00805f9b34fb" -> "Scan Interval Window"
            "00002a50-0000-1000-8000-00805f9b34fb" -> "PnP ID"
            "00002a51-0000-1000-8000-00805f9b34fb" -> "Glucose Feature"
            "00002a52-0000-1000-8000-00805f9b34fb" -> "Record Access Control Point"
            "00002a53-0000-1000-8000-00805f9b34fb" -> "RSC Measurement"
            "00002a54-0000-1000-8000-00805f9b34fb" -> "RSC Feature"
            "00002a55-0000-1000-8000-00805f9b34fb" -> "SC Control Point"
            "00002a56-0000-1000-8000-00805f9b34fb" -> "Digital"
            "00002a57-0000-1000-8000-00805f9b34fb" -> "Digital Output"
            "00002a58-0000-1000-8000-00805f9b34fb" -> "Analog"
            "00002a59-0000-1000-8000-00805f9b34fb" -> "Analog Output"
            "00002a5a-0000-1000-8000-00805f9b34fb" -> "Aggregate"
            "00002a5b-0000-1000-8000-00805f9b34fb" -> "CSC Measurement"
            "00002a5c-0000-1000-8000-00805f9b34fb" -> "CSC Feature"
            "00002a5d-0000-1000-8000-00805f9b34fb" -> "Sensor Location"
            "00002a5e-0000-1000-8000-00805f9b34fb" -> "PLX Spot-Check Measurement"
            "00002a5f-0000-1000-8000-00805f9b34fb" -> "PLX Continuous Measurement Characteristic"
            "00002a60-0000-1000-8000-00805f9b34fb" -> "PLX Features"
            "00002a62-0000-1000-8000-00805f9b34fb" -> "Pulse Oximetry Control Point"
            "00002a63-0000-1000-8000-00805f9b34fb" -> "Cycling Power Measurement"
            "00002a64-0000-1000-8000-00805f9b34fb" -> "Cycling Power Vector"
            "00002a65-0000-1000-8000-00805f9b34fb" -> "Cycling Power Feature"
            "00002a66-0000-1000-8000-00805f9b34fb" -> "Cycling Power Control Point"
            "00002a67-0000-1000-8000-00805f9b34fb" -> "Location and Speed Characteristic"
            "00002a68-0000-1000-8000-00805f9b34fb" -> "Navigation"
            "00002a69-0000-1000-8000-00805f9b34fb" -> "Position Quality"
            "00002a6a-0000-1000-8000-00805f9b34fb" -> "LN Feature"
            "00002a6b-0000-1000-8000-00805f9b34fb" -> "LN Control Point"
            "00002a6c-0000-1000-8000-00805f9b34fb" -> "Elevation"
            "00002a6d-0000-1000-8000-00805f9b34fb" -> "Pressure"
            "00002a6e-0000-1000-8000-00805f9b34fb" -> "Temperature"
            "00002a6f-0000-1000-8000-00805f9b34fb" -> "Humidity"
            "00002a70-0000-1000-8000-00805f9b34fb" -> "True Wind Speed"
            "00002a71-0000-1000-8000-00805f9b34fb" -> "True Wind Direction"
            "00002a72-0000-1000-8000-00805f9b34fb" -> "Apparent Wind Speed"
            "00002a73-0000-1000-8000-00805f9b34fb" -> "Apparent Wind Direction"
            "00002a74-0000-1000-8000-00805f9b34fb" -> "Gust Factor"
            "00002a75-0000-1000-8000-00805f9b34fb" -> "Pollen Concentration"
            "00002a76-0000-1000-8000-00805f9b34fb" -> "UV Index"
            "00002a77-0000-1000-8000-00805f9b34fb" -> "Irradiance"
            "00002a78-0000-1000-8000-00805f9b34fb" -> "Rainfall"
            "00002a79-0000-1000-8000-00805f9b34fb" -> "Wind Chill"
            "00002a7a-0000-1000-8000-00805f9b34fb" -> "Heat Index"
            "00002a7b-0000-1000-8000-00805f9b34fb" -> "Dew Point"
            "00002a7d-0000-1000-8000-00805f9b34fb" -> "Descriptor Value Changed"
            "00002a7e-0000-1000-8000-00805f9b34fb" -> "Aerobic Heart Rate Lower Limit"
            "00002a7f-0000-1000-8000-00805f9b34fb" -> "Aerobic Threshold"
            "00002a80-0000-1000-8000-00805f9b34fb" -> "Age"
            "00002a81-0000-1000-8000-00805f9b34fb" -> "Anaerobic Heart Rate Lower Limit"
            "00002a82-0000-1000-8000-00805f9b34fb" -> "Anaerobic Heart Rate Upper Limit"
            "00002a83-0000-1000-8000-00805f9b34fb" -> "Anaerobic Threshold"
            "00002a84-0000-1000-8000-00805f9b34fb" -> "Aerobic Heart Rate Upper Limit"
            "00002a85-0000-1000-8000-00805f9b34fb" -> "Date of Birth"
            "00002a86-0000-1000-8000-00805f9b34fb" -> "Date of Threshold Assessment"
            "00002a87-0000-1000-8000-00805f9b34fb" -> "Email Address"
            "00002a88-0000-1000-8000-00805f9b34fb" -> "Fat Burn Heart Rate Lower Limit"
            "00002a89-0000-1000-8000-00805f9b34fb" -> "Fat Burn Heart Rate Upper Limit"
            "00002a8a-0000-1000-8000-00805f9b34fb" -> "First Name"
            "00002a8b-0000-1000-8000-00805f9b34fb" -> "Five Zone Heart Rate Limits"
            "00002a8c-0000-1000-8000-00805f9b34fb" -> "Gender"
            "00002a8d-0000-1000-8000-00805f9b34fb" -> "Heart Rate Max"
            "00002a8e-0000-1000-8000-00805f9b34fb" -> "Height"
            "00002a8f-0000-1000-8000-00805f9b34fb" -> "Hip Circumference"
            "00002a90-0000-1000-8000-00805f9b34fb" -> "Last Name"
            "00002a91-0000-1000-8000-00805f9b34fb" -> "Maximum Recommended Heart Rate"
            "00002a92-0000-1000-8000-00805f9b34fb" -> "Resting Heart Rate"
            "00002a93-0000-1000-8000-00805f9b34fb" -> "Sport Type for Aerobic and Anaerobic Thresholds"
            "00002a94-0000-1000-8000-00805f9b34fb" -> "Three Zone Heart Rate Limits"
            "00002a95-0000-1000-8000-00805f9b34fb" -> "Two Zone Heart Rate Limit"
            "00002a96-0000-1000-8000-00805f9b34fb" -> "VO2 Max"
            "00002a97-0000-1000-8000-00805f9b34fb" -> "Waist Circumference"
            "00002a98-0000-1000-8000-00805f9b34fb" -> "Weight"
            "00002a99-0000-1000-8000-00805f9b34fb" -> "Database Change Increment"
            "00002a9a-0000-1000-8000-00805f9b34fb" -> "User Index"
            "00002a9b-0000-1000-8000-00805f9b34fb" -> "Body Composition Feature"
            "00002a9c-0000-1000-8000-00805f9b34fb" -> "Body Composition Measurement"
            "00002a9d-0000-1000-8000-00805f9b34fb" -> "Weight Measurement"
            "00002a9e-0000-1000-8000-00805f9b34fb" -> "Weight Scale Feature"
            "00002a9f-0000-1000-8000-00805f9b34fb" -> "User Control Point"
            "00002aa0-0000-1000-8000-00805f9b34fb" -> "Magnetic Flux Density - 2D"
            "00002aa1-0000-1000-8000-00805f9b34fb" -> "Magnetic Flux Density - 3D"
            "00002aa2-0000-1000-8000-00805f9b34fb" -> "Language"
            "00002aa3-0000-1000-8000-00805f9b34fb" -> "Barometric Pressure Trend"
            "00002aa4-0000-1000-8000-00805f9b34fb" -> "Bond Management Control Point"
            "00002aa5-0000-1000-8000-00805f9b34fb" -> "Bond Management Features"
            "00002aa6-0000-1000-8000-00805f9b34fb" -> "Central Address Resolution"
            "00002aa7-0000-1000-8000-00805f9b34fb" -> "CGM Measurement"
            "00002aa8-0000-1000-8000-00805f9b34fb" -> "CGM Feature"
            "00002aa9-0000-1000-8000-00805f9b34fb" -> "CGM Status"
            "00002aaa-0000-1000-8000-00805f9b34fb" -> "CGM Session Start Time"
            "00002aab-0000-1000-8000-00805f9b34fb" -> "CGM Session Run Time"
            "00002aac-0000-1000-8000-00805f9b34fb" -> "CGM Specific Ops Control Point"
            "00002aad-0000-1000-8000-00805f9b34fb" -> "Indoor Positioning Configuration"
            "00002aae-0000-1000-8000-00805f9b34fb" -> "Latitude"
            "00002aaf-0000-1000-8000-00805f9b34fb" -> "Longitude"
            "00002ab0-0000-1000-8000-00805f9b34fb" -> "Local North Coordinate"
            "00002ab1-0000-1000-8000-00805f9b34fb" -> "Local East Coordinate"
            "00002ab2-0000-1000-8000-00805f9b34fb" -> "Floor Number"
            "00002ab3-0000-1000-8000-00805f9b34fb" -> "Altitude"
            "00002ab4-0000-1000-8000-00805f9b34fb" -> "Uncertainty"
            "00002ab5-0000-1000-8000-00805f9b34fb" -> "Location Name"
            "00002ab6-0000-1000-8000-00805f9b34fb" -> "URI"
            "00002ab7-0000-1000-8000-00805f9b34fb" -> "HTTP Headers"
            "00002ab8-0000-1000-8000-00805f9b34fb" -> "HTTP Status Code"
            "00002ab9-0000-1000-8000-00805f9b34fb" -> "HTTP Entity Body"
            "00002aba-0000-1000-8000-00805f9b34fb" -> "HTTP Control Point"
            "00002abb-0000-1000-8000-00805f9b34fb" -> "HTTPS Security"
            "00002abc-0000-1000-8000-00805f9b34fb" -> "TDS Control"
            "00002abd-0000-1000-8000-00805f9b34fb" -> "OTS Feature"
            "00002abe-0000-1000-8000-00805f9b34fb" -> "Object Name"
            "00002abf-0000-1000-8000-00805f9b34fb" -> "Object Type"
            "00002ac0-0000-1000-8000-00805f9b34fb" -> "Object Size"
            "00002ac1-0000-1000-8000-00805f9b34fb" -> "Object First-Created"
            "00002ac2-0000-1000-8000-00805f9b34fb" -> "Object Last-Modified"
            "00002ac3-0000-1000-8000-00805f9b34fb" -> "Object ID"
            "00002ac4-0000-1000-8000-00805f9b34fb" -> "Object Properties"
            "00002ac5-0000-1000-8000-00805f9b34fb" -> "Object Action Control Point"
            "00002ac6-0000-1000-8000-00805f9b34fb" -> "Object List Control Point"
            "00002ac7-0000-1000-8000-00805f9b34fb" -> "Object List Filter"
            "00002ac8-0000-1000-8000-00805f9b34fb" -> "Object Changed"
            "00002ac9-0000-1000-8000-00805f9b34fb" -> "Resolvable Private Address Only"
            "00002acc-0000-1000-8000-00805f9b34fb" -> "Fitness Machine Feature"
            "00002acd-0000-1000-8000-00805f9b34fb" -> "Treadmill Data"
            "00002ace-0000-1000-8000-00805f9b34fb" -> "Cross Trainer Data"
            "00002acf-0000-1000-8000-00805f9b34fb" -> "Step Climber Data"
            "00002ad0-0000-1000-8000-00805f9b34fb" -> "Stair Climber Data"
            "00002ad1-0000-1000-8000-00805f9b34fb" -> "Rower Data"
            "00002ad2-0000-1000-8000-00805f9b34fb" -> "Indoor Bike Data"
            "00002ad3-0000-1000-8000-00805f9b34fb" -> "Training Status"
            "00002ad4-0000-1000-8000-00805f9b34fb" -> "Supported Speed Range"
            "00002ad5-0000-1000-8000-00805f9b34fb" -> "Supported Inclination Range"
            "00002ad6-0000-1000-8000-00805f9b34fb" -> "Supported Resistance Level Range"
            "00002ad7-0000-1000-8000-00805f9b34fb" -> "Supported Heart Rate Range"
            "00002ad8-0000-1000-8000-00805f9b34fb" -> "Supported Power Range"
            "00002ad9-0000-1000-8000-00805f9b34fb" -> "Fitness Machine Control Point"
            "00002ada-0000-1000-8000-00805f9b34fb" -> "Fitness Machine Status"
            "00002b1d-0000-1000-8000-00805f9b34fb" -> "RC Feature"
            "00002b1e-0000-1000-8000-00805f9b34fb" -> "RC Settings"
            "00002b1f-0000-1000-8000-00805f9b34fb" -> "Reconnection Configuration Control Point"
            else -> "Custom Characteristic"

        }
    }

    fun UIIDFilter(uuid: String): String {

        return when (uuid.substring(0, 4) + uuid.substring(8)) {
            "0000-0000-1000-8000-00805f9b34fb" -> uuid.substring(4, 8).toUpperCase()
            else -> uuid.toUpperCase()

        }
    }




    fun getPropertiesTextViews(properties: Int, context: Context): ArrayList<TouchableTextView> {


        val list: ArrayList<TouchableTextView> = ArrayList()
        var lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        lparams.setMargins(12,0,10,0)



        if (BluetoothGattCharacteristic.PROPERTY_BROADCAST and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "BROADCAST"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }
        if (BluetoothGattCharacteristic.PROPERTY_READ and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "READ"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }
        if (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text = "WRITE NO RESPONSE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }
        if (BluetoothGattCharacteristic.PROPERTY_WRITE and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "WRITE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }


        }
        if (BluetoothGattCharacteristic.PROPERTY_NOTIFY and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "NOTIFY"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }


        }
        if (BluetoothGattCharacteristic.PROPERTY_INDICATE and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text = "INDICATE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }
        if (BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "SIGNED WRITE"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }
        if (BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS and properties != 0) {

            list.add(TouchableTextView(context))
            list.last().text  = "EXTENDED PROPS"
            list.last().typeface= Typeface.DEFAULT_BOLD
            list.last().layoutParams= lparams
            list.last().isClickable=true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list.last().focusable= View.FOCUSABLE
            }

        }

        return list

    }


    fun getProperties(properties: Int): String {

        var s = " "
        if (BluetoothGattCharacteristic.PROPERTY_BROADCAST and properties != 0)
            s += ("BROADCAST ")

        if (BluetoothGattCharacteristic.PROPERTY_READ and properties != 0)

            s += "READ "

        if (BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE and properties != 0)

            s += ("WRITE NO RESPONSE ")

        if (BluetoothGattCharacteristic.PROPERTY_WRITE and properties != 0)
            s += ("WRITE ")

        if (BluetoothGattCharacteristic.PROPERTY_NOTIFY and properties != 0)
            s+=("NOTIFY ")

        if (BluetoothGattCharacteristic.PROPERTY_INDICATE and properties != 0)
            s += ("INDICATE ")

        if (BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE and properties != 0)
            s+=("SIGNED_WRITE ")

        if (BluetoothGattCharacteristic.PROPERTY_EXTENDED_PROPS and properties != 0)

            s += ("EXTENDED_PROPS ")


        return s

    }

    fun byteArrayToHex(a: ByteArray): String {
        val sb = StringBuilder(a.size * 2)
        for (b in a) {
            sb.append("0x")
            sb.append(String.format("%02x", b))
            sb.append(" ")
        }
        return sb.toString()
    }

     fun bytesToUnsignedShort(byte1 : Byte, byte2 : Byte) : Int {

        //litlle andian
        return (((byte2.toInt() and 255) shl 8) or (byte1.toInt() and 255))

    }


}