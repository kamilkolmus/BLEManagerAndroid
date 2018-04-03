package com.izaphe.blemanager.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.Window
import kotlinx.android.synthetic.main.dialog_graph_settings.*
import android.widget.Toast
import com.i7xaphe.blemanager.R


import com.izaphe.blemanager.filters.InputFilterMinMax
import com.izaphe.blemanager.myinterfaces.DialogGraphSettingsInterface


class DialogGraphSettings(context: Context?, dialogGraphSettingsInterface: DialogGraphSettingsInterface, historySize:Int) : Dialog(context) {

    var dialogGraphSettingsInterface =dialogGraphSettingsInterface
    var historySize:Int=historySize

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_graph_settings)

        et_historysize.filters = arrayOf<InputFilter>(InputFilterMinMax(1, 1000))

        et_historysize.setText(historySize.toString())

        b_save_graph_settings.setOnClickListener({
            try {
                historySize = Integer.parseInt(et_historysize.text.toString())
                if(historySize<10){
                    historySize=10
                    Toast.makeText(context, "Min history size is 10", Toast.LENGTH_SHORT).show()
                }
                dialogGraphSettingsInterface.historySize(historySize)
                dismiss()
                cancel()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "NumberFormatException: history size", Toast.LENGTH_SHORT).show()
            }

        })

        b_cancel_graph_dialog.setOnClickListener({
            dismiss()
            cancel()
        })
    }


}