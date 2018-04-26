package com.izaphe.blemanager.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.RadioGroup
import com.i7xaphe.blemanager.R
import com.izaphe.blemanager.myinterfaces.DialogCharateristicSettingsInterface

import kotlinx.android.synthetic.main.dialog_charateristic_settings.*
import kotlinx.android.synthetic.main.dialog_standard_charateristic_settings.*


class DialogStandardCharateristicsSettings(context: Context?, dialogCharacteristicSettingsInterface: DialogCharateristicSettingsInterface, function:String) : Dialog(context) {


    var dialogCharacteristicSettingsInterface =dialogCharacteristicSettingsInterface
    var function:String=function

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_standard_charateristic_settings)

        radio_group1.setOnCheckedChangeListener({ radioGroup: RadioGroup, id: Int ->
            when(id){
                rb_none1.id->{
                    function=rb_none1.text.toString()
                    setInfo(function)
                }
                rb_char_value1.id->{
                    function=rb_char_value1.text.toString()
                    setInfo(function)
                }

            }
        })
        when(function){
            rb_none1.text.toString()->{
                rb_none1.isChecked=true
                setInfo(function)
            }
            rb_char_value1.text.toString()->{
                rb_char_value1.isChecked=true
                setInfo(function)
            }

        }

        save1.setOnClickListener({
            dialogCharacteristicSettingsInterface.onSaveClick(function)
            dismiss()
            cancel()
        })
        cancel1.setOnClickListener({
            dismiss()
            cancel()
        })
    }

    fun setInfo(function:String){
        when(function){
            rb_none1.text.toString()->{ info1.text="Packages from a given characteristic will be omitted"}
            rb_char_value1.text.toString()->{ info1.text="Data from the package will be displayed on the chart" }
        }
    }

}