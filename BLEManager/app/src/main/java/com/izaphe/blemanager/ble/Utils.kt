package com.izaphe.blemanager.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import com.izaphe.blemanager.views.TouchableTextView

object Utils {


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
}