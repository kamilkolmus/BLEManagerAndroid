package com.i7xaphe.blemanager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_graph.*
import android.os.Build
import android.support.annotation.RequiresApi
import com.i7xaphe.blemanager.MyObject.multiDeviceCharCollection


/**
 * Created by Kamil on 2018-03-17.
 */
class GraphActivity : AppCompatActivity(){

    var counter=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        registerReceiver(broadcastReceiver, makeIntentFilter())

    }



    private val broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            counter++
            if(action == ACTION_SEND_CHARACTERISTIC_VALUE){

                ed_test_area.append( "\n"+counter+"\n"+
                        intent.getStringExtra(EXTRA_DEVICE_NAME) +" "+"\n"+
                        intent.getStringExtra(EXTRA_SERVICE_NAME)+" "+"\n"+
                        intent.getStringExtra(EXTRA_CHARACTERISTIC_NAME)+" "+"\n"+
                        intent.getStringExtra(EXTRA_DATA)+"\n")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        println("START")
        ed_test_area2.text.clear()
        //raed data from common collection for all FragmentServices
        //this collection holds information about the characteristics from which data will come to be displayed on the chart
        multiDeviceCharCollection.forEach { key, value -> ed_test_area2.append("${key.first} ${key.second.first} ${key.second.second}= ${value.deviceName} ${value.serviceName} ${value.characteristicName} \n") }
        println("STOP ${multiDeviceCharCollection.size}")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
      //  super.onBackPressed()
        moveTaskToBack(true)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    companion object {
        val ACTION_SEND_CHARACTERISTIC_VALUE = "ACTION_SEND_CHARACTERISTIC_VALUE"
        val EXTRA_DATA = "EXTRA_DATA"
        val EXTRA_SERVICE_INDEX = "EXTRA_SERVICE_INDEX"
        val EXTRA_CHARACTERISTIC_INDEX = "EXTRA_CHARACTERISTIC_INDEX"
        val EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID"
        val EXTRA_DEVICE_NAME = "EXTRA_DEVICE_NAME"
        val EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS"
        val EXTRA_SERVICE_NAME = "EXTRA_SERVICE_NAME"
        val EXTRA_CHARACTERISTIC_NAME = "EXTRA_CHARACTERISTIC_NAME"

    }

    private fun makeIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()

        intentFilter.addAction(ACTION_SEND_CHARACTERISTIC_VALUE)

        return intentFilter
    }
}