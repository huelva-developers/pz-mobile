package com.huelvadevelopers.proyectozero

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.huelvadevelopers.proyectozero.model.BankAccount
import kotlinx.android.synthetic.main.accounts_fragment.view.*
import kotlinx.android.synthetic.main.bank_account_layout.view.*
import android.widget.TextView
import java.text.NumberFormat


/**
 * Created by DrAP on 08/07/2017.
 */
class BankAccountAdapter(val mContext : Context, val accounts : Array<BankAccount>) :
    RecyclerView.Adapter<BankAccountAdapter.ViewHolder>(){
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = accounts[position]
        holder.nameTextView.text = account.name
        holder.descriptionTextView.text = account.description
        val format = NumberFormat.getInstance(mContext.resources.configuration.locale)
        val money = format.format(account.balance)
        holder.balanceTextView.text = money.toString() + account.currency
        holder.colorImageView.setImageResource(mContext.resources.obtainTypedArray(R.array.account_color).getResourceId(account.color, -1))
        if(account.balance >= 0)
            holder.balanceTextView!!.setTextColor(mContext.resources.getColor(R.color.colorPositiveBalance))
        else
            holder.balanceTextView!!.setTextColor(mContext.resources.getColor(R.color.colorNegativeBalance))

        holder.mLayout.setOnLongClickListener {
            //Log.v("click", "long click en text")
            val v = (mContext as Activity).window.decorView.findViewById(android.R.id.content)
            val params = v.account_container_layout.layoutParams
            (params as LinearLayout.LayoutParams).weight = 90f
            v.account_container_layout.layoutParams = params
            v.remove_bank_account.visibility = View.VISIBLE
            var item = ClipData.Item(account.id.toString())

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            var dragData: ClipData = ClipData(account.name, Array(1, { ClipDescription.MIMETYPE_TEXT_PLAIN }), item)
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
                val params = contentView.account_container_layout.layoutParams
                (params as LinearLayout.LayoutParams).weight=100f
                contentView.account_container_layout.layoutParams = params
                contentView.remove_bank_account.visibility=View.GONE
                (mContext as MainActivity).goSection(2)
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

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var v = LayoutInflater.from(mContext).inflate(R.layout.bank_account_layout, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(var mLayout : View) : RecyclerView.ViewHolder(mLayout) {
        var descriptionTextView : TextView = mLayout.bank_account_description
        var colorImageView : ImageView = mLayout.bank_account_color
        var nameTextView: TextView = mLayout.bank_account_name
        var balanceTextView: TextView = mLayout.bank_account_balance
    }
}