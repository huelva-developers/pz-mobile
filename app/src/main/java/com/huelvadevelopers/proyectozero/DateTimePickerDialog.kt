package com.huelvadevelopers.proyectozero

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.huelvadevelopers.proyectozero.model.BankAccount
import com.huelvadevelopers.proyectozero.model.Category
import kotlinx.android.synthetic.main.add_bank_account_dialog.view.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.date_picker_layout.view.*
import kotlinx.android.synthetic.main.time_picker_layout.view.*

/**
 * Created by DrAP on 04/07/2017.
 */

class DateTimePickerDialog(context : Context) : Builder( context ){

    val mContex = context
    lateinit var mAlertDialog : AlertDialog
    var mode : Int = 0 //0 = Date, 1 = Time
    lateinit var dpDate : DatePicker
    lateinit var tpTime : TimePicker


    override fun show(): AlertDialog {
        if(mode==0) {
            val dialogView = LayoutInflater.from(mContex).inflate(R.layout.date_picker_layout, null)
            setView(dialogView)

            //Name EditText
            dpDate = dialogView.transaction_date_picker

            setTitle("Select date")
        }
        else {
            val dialogView = LayoutInflater.from(mContex).inflate(R.layout.time_picker_layout, null)
            setView(dialogView)

            //Name EditText
            tpTime = dialogView.transaction_time_picker

            setTitle("Select time")
        }


        setPositiveButton("Done") { dialog, whichButton ->
            //Vacio porque abajo lo sobrescribimos para decidir si hacer dismiss o no
        }
        setNegativeButton("Cancel") { dialog, whichButton ->
            //pass
        }
        mAlertDialog = super.show()
        return mAlertDialog
    }

    fun show(mode : Int) : AlertDialog {
        this.mode = mode
        return show()
    }
}