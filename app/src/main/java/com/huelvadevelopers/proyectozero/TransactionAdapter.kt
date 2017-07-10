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
import kotlinx.android.synthetic.main.transactions_fragment.view.*
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
            holder.amountTextView!!.setTextColor(context.resources.getColor(R.color.colorAccent))
        else
            holder.amountTextView!!.setTextColor(context.resources.getColor(R.color.colorNegativeBalance))

        view.setBackgroundColor(context.resources.getColor(
                context.resources.obtainTypedArray(R.array.account_color_light).getResourceId(transaction.bankAccount.color, -1)))

        view.setOnLongClickListener {
            //Log.v("click", "long click en text")
            val v = (context as Activity).window.decorView.findViewById(android.R.id.content)
            val params = v.transaction_container_layout.layoutParams
            (params as LinearLayout.LayoutParams).weight = 90f
            v.transaction_container_layout.layoutParams = params
            v.remove_transaction.visibility = View.VISIBLE
            var item = ClipData.Item(transaction.id.toString())

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            var dragData: ClipData = ClipData(transaction.description, Array(1, { ClipDescription.MIMETYPE_TEXT_PLAIN }), item)
            //Añadimos el numero de subcategorías para despues aceptar el cambio de padre o no

            // Instantiates the drag shadow builder.
            var myShadow = CustomDragShadowBuilder(view)
            view.layoutParams = AbsListView.LayoutParams(-1, 1)
            view.visibility = View.GONE

            // Starts the drag

            it.startDrag(dragData, // the data to be dragged
                    myShadow, // the drag shadow builder
                    null, // no need to use local data
                    0          // flags (not currently used, set to 0)
            )
            true
        }
        view.setOnDragListener { v, event ->
            if(event.action==DragEvent.ACTION_DRAG_ENDED){
                val contentView = (context as Activity).window.decorView.findViewById(android.R.id.content)
                val params = contentView.transaction_container_layout.layoutParams
                (params as LinearLayout.LayoutParams).weight=100f
                contentView.transaction_container_layout.layoutParams = params
                contentView.remove_transaction.visibility=View.GONE
                (context as MainActivity).goSection(1)
                // Does a getResult(), and displays what happened.
                if (event.getResult()) {
                    //Toast.makeText(context, "The drop was handled.", Toast.LENGTH_LONG).show()
                    Log.v("drag",  "The drop was handled.")

                } else {
                    //Toast.makeText(context, "The drop didn't work.", Toast.LENGTH_LONG).show()
                    Log.v("drag",  "The drop didn't work.")
                }

                // returns true; the value is ignored.
                true
            }
            true
        }

        return view
    }

    private class ViewHolder {
        var categoryIconImageView : ImageView? = null
        var descriptionTextView: TextView? = null
        var dateTextView: TextView? = null
        var amountTextView: TextView? = null
    }
}