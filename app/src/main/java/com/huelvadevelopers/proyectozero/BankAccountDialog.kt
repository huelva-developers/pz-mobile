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

/**
 * Created by DrAP on 04/07/2017.
 */

class BankAccountDialog(context : Context) : Builder( context ){

    val mContex = context
    lateinit var mAlertDialog : AlertDialog
    lateinit var edtName : EditText
    lateinit var edtDescription : EditText
    lateinit var edtBalance : EditText
    lateinit var spinner : Spinner
    var currentAccount : BankAccount? = null // Solo se usa para editar cuentas


    override fun show(): AlertDialog {
        val dialogView = LayoutInflater.from(mContex).inflate(R.layout.add_bank_account_dialog, null)
        setView(dialogView)

        //Name EditText
        edtName = dialogView.add_bank_account_name as EditText

        //Description EditText
        edtDescription = dialogView.add_bank_account_description as EditText

        //Balance EditText
        edtBalance = dialogView.add_bank_account_balance as EditText

        //Parent Spinner
        spinner = dialogView.add_bank_account_currency as Spinner

        var array = arrayOf( "\u20AC" , "\u00A3" , "\u0024" )
        val adapter : ArrayAdapter<String> = ArrayAdapter(mContex, android.R.layout.simple_spinner_item, array)
        spinner.adapter = adapter

        setTitle("New Bank Account")
        setPositiveButton("Done") { dialog, whichButton ->
            //Vacio porque abajo lo sobrescribimos para decidir si hacer dismiss o no
        }
        setNegativeButton("Cancel") { dialog, whichButton ->
            //pass
        }
        if(currentAccount != null){ //Si currentAccount no es nulo es porque estamos editanto
            edtName.setText(currentAccount!!.name)
            edtDescription.setText(currentAccount!!.description)
            dialogView.add_bank_account_balance_layout.visibility = View.GONE
            setTitle("Edit Bank Account")
        }
        mAlertDialog = super.show()
        return mAlertDialog
    }
}