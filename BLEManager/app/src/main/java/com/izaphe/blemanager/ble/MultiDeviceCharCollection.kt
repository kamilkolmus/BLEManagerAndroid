package com.izaphe.blemanager.ble


import com.izaphe.blemanager.myinterfaces.MultiDeviceCharCollectionInterface
import java.util.HashMap



object MultiDeviceCharCollection {

    //common collection storing information about the characteristics
    //fragmentBleService can add information here
    //GraphActivity can read them
    var multiDeviceCharCollection = HashMap<Pair<Int,Pair<Int,Int>>,BleChrateristicInfo>()

    var multiDeviceCharCollectionInterface: MultiDeviceCharCollectionInterface?=null

    fun addtoMultiDeviceCharCollection(key:Pair<Int,Pair<Int,Int>>,info: BleChrateristicInfo){
        multiDeviceCharCollection.put(key,info)
        if(multiDeviceCharCollectionInterface!=null){
            multiDeviceCharCollectionInterface!!.add(key,info)
        }
    }

    fun removeMultiDeviceCharCollection(key:Pair<Int,Pair<Int,Int>>){
        multiDeviceCharCollection.remove(key)
        if(multiDeviceCharCollectionInterface!=null){
            multiDeviceCharCollectionInterface!!.remove(key)

        }

    }
    fun setInterface(multiDeviceCharCollectionInterface: MultiDeviceCharCollectionInterface){
        this.multiDeviceCharCollectionInterface=multiDeviceCharCollectionInterface
    }


}