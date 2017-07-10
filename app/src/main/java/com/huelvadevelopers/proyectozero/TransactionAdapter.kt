package com.huelvadevelopers.proyectozero

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.huelvadevelopers.proyectozero.model.BankAccount
import kotlinx.android.synthetic.main.accounts_fragment.view.*
import android.widget.TextView
import android.widget.AbsListView
import com.huelvadevelopers.proyectozero.model.Transaction
import kotlinx.android.synthetic.main.add_transaction_dialog.view.*
import kotlinx.android.synthetic.main.transaction_layout.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*


/**
 * Created by DrAP on 08/07/2017.
 */

class TransactionAdapter(context : Activity, val transactions : ArrayList<Transaction>)
    : ArrayAdapter<Transaction>(context, 0, transactions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View
        var holder : ViewHolder
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.transaction_layout, null, false)
            holder = ViewHolder()
            holder.categoryIconImageView = view.transaction_category_icon as ImageView
            holder.descriptionTextView = view.transaction_description as TextView
            holder.dateTextView = view.transaction_date as TextView
            holder.amountTextView = view.transaction_amount as TextView
            view.tag = holder

        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val transaction = transactions[position]
        holder.descriptionTextView!!.text = transaction.description
        val format = NumberFormat.getInstance(context.resources.configuration.locale)
        val money = format.format(transaction.amount)
        holder.amountTextView!!.text = money.toString() + transaction.bankAccount.currency
        if(transaction.category != null)
            holder.categoryIconImageView!!.setImageResource(
                    context.resources.obtainTypedArray(R.array.icons).getResourceId(transaction.category.icon, -1))
        else
            holder.categoryIconImageView!!.visibility = View.INVISIBLE

        holder.dateTextView!!.text = DateFormat.getDateTimeInstance().format(transaction.date)

        if(transaction.amount >= 0)
            holder.amountTextView!!.setTextColor(context.resources.getColor(R.color.colorPositiveBalance))
        else
            holder.amountTextView!!.setTextColor(context.resources.getColor(R.color.colorNegativeBalance))

        view.setBackgroundColor(context.resources.getColor(
                context.resources.obtainTypedArray(R.array.account_color_light).getResourceId(transaction.bankAccount.color, -1)))

        return view
    }

    private class ViewHolder {
        var categoryIconImageView : ImageView? = null
        var descriptionTextView: TextView? = null
        var dateTextView: TextView? = null
        var amountTextView: TextView? = null
    }
}