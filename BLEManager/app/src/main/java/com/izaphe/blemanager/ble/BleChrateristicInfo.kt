package com.izaphe.blemanager.ble

/**
 * Created by Kamil on 2018-03-19.
 */
class BleChrateristicInfo(deviceName: String, serviceName: String, characteristicName: String, characteristicUUID: String) {
    var deviceName=deviceName
    var characteristicName=characteristicName
    var characteristicUIID=characteristicUUID
    var serviceName=serviceName
    var dataRoleInterpratation: String="NONE"
}