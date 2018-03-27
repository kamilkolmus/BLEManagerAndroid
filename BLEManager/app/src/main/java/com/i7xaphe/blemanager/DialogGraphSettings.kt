package com.i7xaphe.blemanager

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.Window
import kotlinx.android.synthetic.main.dialog_graph_settings.*
import android.widget.Toast


interface DialogGraphSettingsInterface{
    fun historySize(historySiza:Int)
}

class DialogGraphSettings(context: Context?, dialogGraphSettingsInterface:DialogGraphSettingsInterface, historySize:Int) : Dialog(context) {
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
                    Toast.makeText(context, "MIN HISTORY SIZE is 10", Toast.LENGTH_SHORT).show()
                }
                dialogGraphSettingsInterface.historySize(historySize)
                dismiss()
                cancel()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "NumberFormatException: HISTORY SIZE", Toast.LENGTH_SHORT).show()
            }

        })

        b_cancel_graph_dialog.setOnClickListener({
            dismiss()
            cancel()
        })
    }


}