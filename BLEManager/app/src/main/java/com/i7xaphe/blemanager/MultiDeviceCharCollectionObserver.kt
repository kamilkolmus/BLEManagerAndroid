package com.i7xaphe.blemanager

import java.util.HashMap

/**
 * Created by Kamil on 2018-03-04.
 */

interface MultiDeviceCharCollectionInterface{
    fun add(key:Pair<Int,Pair<Int,Int>>,info: GraphChrateristicInfo){

    }

    fun remove(key: Pair<Int, Pair<Int, Int>>) {

    }
}

object MultiDeviceCharCollectionObserver {

    //common collection storing information about the characteristics
    //fragmentBleService can add information here
    //GraphActivity can read them
    var multiDeviceCharCollection = HashMap<Pair<Int,Pair<Int,Int>>,GraphChrateristicInfo>()

    var multiDeviceCharCollectionInterface:MultiDeviceCharCollectionInterface?=null

    fun addtoMultiDeviceCharCollection(key:Pair<Int,Pair<Int,Int>>,info: GraphChrateristicInfo){
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
    fun setInterface(multiDeviceCharCollectionInterface:MultiDeviceCharCollectionInterface){
        this.multiDeviceCharCollectionInterface=multiDeviceCharCollectionInterface
    }


}