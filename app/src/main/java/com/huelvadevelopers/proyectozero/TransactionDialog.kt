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
import com.huelvadevelopers.proyectozero.model.Transaction
import kotlinx.android.synthetic.main.add_bank_account_dialog.view.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*
import kotlinx.android.synthetic.main.add_transaction_dialog.view.*
import java.text.DateFormat
import java.util.*

/**
 * Created by DrAP on 04/07/2017.
 */

class TransactionDialog(context : Context) : Builder( context ){

    val mContex = context
    lateinit var mAlertDialog : AlertDialog
    lateinit var edtDescription : EditText
    lateinit var edtAmount : EditText
    lateinit var tvDatetime : TextView
    lateinit var spnBankAccount : Spinner
    lateinit var spnCategory : Spinner
    var accounts : ArrayList<String>? = null
    var categories : ArrayList<String>? = null
    var currentTransaction : Transaction? = null // Solo se usa para editar transacciones


    override fun show(): AlertDialog {
        val dialogView = LayoutInflater.from(mContex).inflate(R.layout.add_transaction_dialog, null)
        setView(dialogView)

        //BankAccount Spinner
        spnBankAccount = dialogView.add_transaction_bank_account
        accounts = (mContex as MainActivity).databaseManager.getBankAccountsNames()
        val spnBankAccountAdapter : ArrayAdapter<String> = ArrayAdapter(mContex,android.R.layout.simple_spinner_item, accounts)
        spnBankAccount.adapter = spnBankAccountAdapter

        //Category Spinner
        spnCategory = dialogView.add_transaction_category
        categories = (mContex as MainActivity).databaseManager.getCategoriesName()
        val spnCategoryAdapter : ArrayAdapter<String> = ArrayAdapter(mContex,android.R.layout.simple_spinner_item, categories)
        spnCategory.adapter = spnCategoryAdapter

        //Description EditText
        edtDescription = dialogView.add_transaction_description as EditText

        //Amount EditText
        edtAmount = dialogView.add_transaction_amount as EditText

        //Datetime EditText
        tvDatetime = dialogView.add_transaction_date as TextView
        tvDatetime.text = DateFormat.getDateTimeInstance().format(Date())

        setTitle("New Transaction")
        setPositiveButton("Done") { dialog, whichButton ->
            //Vacio porque abajo lo sobrescribimos para decidir si hacer dismiss o no
        }
        setNegativeButton("Cancel") { dialog, whichButton ->
            //pass
        }
       /* if(currentTransaction != null){ //Si currentAccount no es nulo es porque estamos editanto
            edtName.setText(currentAccount!!.name)
            edtDescription.setText(currentAccount!!.description)
            dialogView.add_bank_account_balance_layout.visibility = View.GONE
            setTitle("Edit Bank Account")
        }*/
        mAlertDialog = super.show()
        return mAlertDialog
    }
}