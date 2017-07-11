package com.huelvadevelopers.proyectozero

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.huelvadevelopers.proyectozero.model.Transaction
import kotlinx.android.synthetic.main.transaction_layout.view.*
import kotlinx.android.synthetic.main.transactions_fragment.view.*
import java.text.DateFormat
import java.text.NumberFormat


/**
 * Created by DrAP on 08/07/2017.
 */

class TransactionAdapter(val mContext : Context, val transactions : Array<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : TransactionAdapter.ViewHolder{
        var v = LayoutInflater.from(mContext).inflate(R.layout.transaction_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.descriptionTextView.text = transaction.description
        val format = NumberFormat.getInstance(mContext.resources.configuration.locale)
        val money = format.format(transaction.amount)
        holder.amountTextView!!.text = money.toString() + transaction.bankAccount.currency
        if(transaction.category != null)
            holder.categoryIconImageView!!.setImageResource(
                    mContext.resources.obtainTypedArray(R.array.icons).getResourceId(transaction.category.icon, -1))
        else
            holder.categoryIconImageView!!.visibility = View.INVISIBLE

        holder.dateTextView!!.text = DateFormat.getDateTimeInstance().format(transaction.date)

        if(transaction.amount >= 0)
            holder.amountTextView!!.setTextColor(mContext.resources.getColor(R.color.colorAccent))
        else
            holder.amountTextView!!.setTextColor(mContext.resources.getColor(R.color.colorNegativeBalance))

        (holder.mLayout as CardView).setCardBackgroundColor(mContext.resources.getColor(
                mContext.resources.obtainTypedArray(R.array.account_color_light).getResourceId(transaction.bankAccount.color, -1)))

        holder.mLayout.setOnLongClickListener {
            //Log.v("click", "long click en text")
            val v = (mContext as Activity).window.decorView.findViewById(android.R.id.content)
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
            var myShadow = CustomDragShadowBuilder(holder.mLayout)
            val layoutParams = holder.mLayout.layoutParams
            layoutParams.height = 0
            holder.mLayout.layoutParams = layoutParams
            holder.mLayout.visibility = View.GONE

            // Starts the drag

            it.startDrag(dragData, // the data to be dragged
                    myShadow, // the drag shadow builder
                    null, // no need to use local data
                    0          // flags (not currently used, set to 0)
            )
            true
        }
        holder.mLayout.setOnDragListener { v, event ->
            if(event.action==DragEvent.ACTION_DRAG_ENDED){
                val contentView = (mContext as Activity).window.decorView.findViewById(android.R.id.content)
                val params = contentView.transaction_container_layout.layoutParams
                (params as LinearLayout.LayoutParams).weight=100f
                contentView.transaction_container_layout.layoutParams = params
                contentView.remove_transaction.visibility=View.GONE
                (mContext as MainActivity).goSection(1)
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
    }

    class ViewHolder(var mLayout: View) : RecyclerView.ViewHolder(mLayout){
        var descriptionTextView : TextView = mLayout.transaction_description
        var categoryIconImageView : ImageView = mLayout.transaction_category_icon
        var dateTextView: TextView = mLayout.transaction_date
        var amountTextView: TextView = mLayout.transaction_amount

    }
}
