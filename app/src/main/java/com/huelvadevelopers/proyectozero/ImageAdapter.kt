package com.huelvadevelopers.proyectozero

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.view.View
import android.widget.ImageView.ScaleType
import android.view.ViewGroup
import android.widget.*


/**
 * Created by DrAP on 02/07/2017.
 */

/**
 * @param type si tipo == 1 los recursos son los iconos de las categorías,
 *      si tipo == 2 son los colores de las cuentas.
 */
class ImageAdapter(private val mContext: Context, type : Int) : BaseAdapter() {

    //Icono seleccionado
    var selectionId = 0

    // references to our images
    val mThumbIds : TypedArray
    init {
        if(type==1)
            mThumbIds = mContext.resources.obtainTypedArray(R.array.icons)
        else
            mThumbIds = mContext.resources.obtainTypedArray(R.array.account_color)
    }

    override fun getCount(): Int {
        return mThumbIds.length()
    }

    override fun getItem(position: Int): Any? {
        return mThumbIds.getResourceId(position,-1)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.adjustViewBounds = true
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8)
        } else {
            imageView = (convertView as ImageView?)!!
        }
        imageView.setImageResource(mThumbIds.getResourceId(position,-1))
        imageView.setOnTouchListener { v, event ->
            selectionId = position
            notifyDataSetChanged()
            false
        }
        //Si es el icono seleccionado actualmente le ponemos color de fondo
        if(selectionId==position)
            imageView.setBackgroundColor(mContext.resources.getColor(R.color.colorCategorySelector))
        else
            imageView.setBackgroundColor(Color.TRANSPARENT)
        return imageView
    }
}