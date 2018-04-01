package com.izaphe.blemanager.myinterfaces

import com.izaphe.blemanager.ble.BleChrateristicInfo


interface MultiDeviceCharCollectionInterface{
    fun add(key:Pair<Int,Pair<Int,Int>>,info: BleChrateristicInfo){

    }

    fun remove(key: Pair<Int, Pair<Int, Int>>) {

    }
}