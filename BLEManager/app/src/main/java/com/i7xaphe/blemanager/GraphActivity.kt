package com.i7xaphe.blemanager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_graph.*
import java.util.*

/**
 * Created by Kamil on 2018-03-17.
 */
class GraphActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        registerReceiver(broadcastReceiver, makeIntentFilter())
    }



    private val broadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if(action == ACTION_SEND_CHARATERISTIC_VALUE){
                tv_test.text= """ ${intent.getIntExtra(EXTRA_DEVICE_ID, -1)} ${intent.getIntExtra(EXTRA_SERVICE_INDEX, -1)} ${intent.getIntExtra(EXTRA_CHARATERISTIC_INDEX, -1)} ${intent.getStringExtra(EXTRA_DATA)}"""
            }
            //check if data comes from the device belonging to this fragment
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    companion object {
        val ACTION_SEND_CHARATERISTIC_VALUE = "ACTION_SEND_CHARATERISTIC_VALUE"
        val EXTRA_DATA = "EXTRA_DATA"
        val EXTRA_SERVICE_INDEX = "EXTRA_SERVICE_INDEX"
        val EXTRA_CHARATERISTIC_INDEX = "EXTRA_CHARATERISTIC_INDEX"
        val EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID"
    }

    private fun makeIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()

        intentFilter.addAction(ACTION_SEND_CHARATERISTIC_VALUE)

        return intentFilter
    }
}