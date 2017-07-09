package com.huelvadevelopers.proyectozero

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.huelvadevelopers.proyectozero.model.BankAccount
import kotlinx.android.synthetic.main.accounts_fragment.view.*
import kotlinx.android.synthetic.main.bank_account_layout.view.*
import android.widget.TextView



/**
 * Created by DrAP on 08/07/2017.
 */

class BankAccountAdapter(context : Activity, val accounts : ArrayList<BankAccount>)
    : ArrayAdapter<BankAccount>(context, 0, accounts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View
        var holder : ViewHolder
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.bank_account_layout, null, false)
            holder = ViewHolder()
            holder.nameTextView = view.bank_account_name as TextView
            holder.descriptionTextView = view.bank_account_description as TextView
            holder.balanceTextView = view.bank_account_balance as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val account = accounts[position]
        holder.nameTextView!!.text = account.name
        holder.descriptionTextView!!.text = account.description
        holder.balanceTextView!!.text = account.balance.toString()+account.currency
        if(account.balance >= 0)
            holder.balanceTextView!!.setTextColor(context.resources.getColor(R.color.colorPositiveBalance))
        else
            holder.balanceTextView!!.setTextColor(context.resources.getColor(R.color.colorNegativeBalance))



        return view
    }

    private class ViewHolder {
        var nameTextView: TextView? = null
        var descriptionTextView: TextView? = null
        var balanceTextView: TextView? = null
    }
}