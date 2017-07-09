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
import kotlinx.android.synthetic.main.bank_account_layout.view.*
import android.widget.TextView
import android.widget.AbsListView




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
            holder.colorImageView = view.bank_account_color as ImageView
            view.tag = holder

        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val account = accounts[position]
        holder.nameTextView!!.text = account.name
        holder.descriptionTextView!!.text = account.description
        holder.balanceTextView!!.text = account.balance.toString()+account.currency
        holder.colorImageView!!.setImageResource(context.resources.obtainTypedArray(R.array.account_color).getResourceId(account.color, -1))
        if(account.balance >= 0)
            holder.balanceTextView!!.setTextColor(context.resources.getColor(R.color.colorPositiveBalance))
        else
            holder.balanceTextView!!.setTextColor(context.resources.getColor(R.color.colorNegativeBalance))

        view.setOnLongClickListener {
            //Log.v("click", "long click en text")
            val v = (context as Activity).window.decorView.findViewById(android.R.id.content)
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
                val params = contentView.account_container_layout.layoutParams
                (params as LinearLayout.LayoutParams).weight=100f
                contentView.account_container_layout.layoutParams = params
                contentView.remove_bank_account.visibility=View.GONE
                (context as MainActivity).goSection(2)
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
        var nameTextView: TextView? = null
        var descriptionTextView: TextView? = null
        var balanceTextView: TextView? = null
        var colorImageView : ImageView? = null
    }
}