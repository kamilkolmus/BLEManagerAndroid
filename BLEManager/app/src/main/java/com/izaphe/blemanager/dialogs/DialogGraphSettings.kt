package com.izaphe.blemanager.dialogs


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.InflateException
import android.view.Window
import com.i7xaphe.blemanager.R



class DialogGraphSettings(context: Context?,activity: AppCompatActivity) : Dialog(context) {


    val activity=activity
    var removeFragment=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
       // setTitle("Settings")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            setContentView(R.layout.dialog_graph_settings_fragment)
        }catch (e: InflateException){
            e.printStackTrace()
            cancel()
            removeFragment=false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if(removeFragment){
            val fm = activity.supportFragmentManager
            val xmlFragment = fm.findFragmentById(R.id.fragment_graph_settings)
            if (xmlFragment != null) {
                fm.beginTransaction().remove(xmlFragment).commit()
            }
        }

    }
    companion object {
        val TAG:String=DialogGraphSettings::class.java.simpleName
    }

}