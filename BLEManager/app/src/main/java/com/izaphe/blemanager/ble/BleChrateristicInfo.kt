package com.izaphe.blemanager.ble

/**
 * Created by Kamil on 2018-03-19.
 */
class BleChrateristicInfo(deviceName: String, characteristicName: String, characteristicUUID: String,standardCharacteristic:Boolean) {
    var deviceName=deviceName
    var characteristicName=characteristicName
    var characteristicUIID=characteristicUUID
    var standardCharacteristic=standardCharacteristic
    var dataRoleInterpratation: String="NONE"
}