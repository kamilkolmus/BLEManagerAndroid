package com.izaphe.blemanager.dialogs


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.InflateException
import android.view.Window
import com.i7xaphe.blemanager.R

class DialogGraphSettings(context: Context?,activity: AppCompatActivity) : Dialog(context) {


    val activity=activity
    var removeFragment=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            setContentView(R.layout.dialog_graph_settings_fragment)

            val displayMetrics = DisplayMetrics()
            window!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            window!!.setLayout((width * 0.9).toInt(), (height * 0.8).toInt())

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