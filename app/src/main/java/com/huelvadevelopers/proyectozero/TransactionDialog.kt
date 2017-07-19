package com.huelvadevelopers.proyectozero

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*



/**
 * Created by DrAP on 04/07/2017.
 */

class TransactionDialog(context : Context) : Builder( context ){

    val mContex = context
    lateinit var mAlertDialog : AlertDialog
    lateinit var edtDescription : EditText
    lateinit var edtAmount : EditText
    lateinit var tvDate : TextView
    lateinit var tvTime : TextView
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
        spnBankAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnBankAccount.adapter = spnBankAccountAdapter

        //Category Spinner
        spnCategory = dialogView.add_transaction_category
        categories = (mContex as MainActivity).databaseManager.getCategoriesName()
        val spnCategoryAdapter : ArrayAdapter<String> = ArrayAdapter(mContex,android.R.layout.simple_spinner_item, categories)
        spnCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategory.adapter = spnCategoryAdapter

        //Description EditText
        edtDescription = dialogView.add_transaction_description as EditText

        //Amount EditText
        edtAmount = dialogView.add_transaction_amount as EditText

        //Date EditText
        tvDate = dialogView.add_transaction_date as TextView
        tvDate.text = DateFormat.getDateInstance().format(Date())
        tvDate.setOnClickListener {
            val dialog = DateTimePickerDialog(context)
            dialog.show(0)
            var calendar = Calendar.getInstance()
            calendar.time = DateFormat.getDateInstance().parse(tvDate.text.toString())
            dialog.dpDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dialog.mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                dialog.mAlertDialog.dismiss()
                val day = dialog.dpDate.dayOfMonth
                val month = dialog.dpDate.month
                val year = dialog.dpDate.year

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                tvDate.text = DateFormat.getDateInstance().format(calendar.time)
                Log.v("date", dialog.dpDate.year.toString())
            }
        }

        //Time EditText
        tvTime = dialogView.add_transaction_time as TextView
        tvTime.text = SimpleDateFormat("H:mm").format(Date().time)
        tvTime.setOnClickListener {
            val dialog = DateTimePickerDialog(context)
            dialog.show(1)
            var calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("H:mm").parse(tvTime.text.toString())
            dialog.tpTime.currentHour =calendar.get(Calendar.HOUR_OF_DAY)
            dialog.tpTime.currentMinute =calendar.get(Calendar.MINUTE)
            dialog.mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                dialog.mAlertDialog.dismiss()
                val hours = dialog.tpTime.currentHour
                val minutes = dialog.tpTime.currentMinute

                val calendar = Calendar.getInstance()
                calendar.set(0 , 0, 0, hours, minutes, 0)
                tvTime.text = SimpleDateFormat("H:mm").format(calendar.time)
            }
        }

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