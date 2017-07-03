package com.huelvadevelopers.proyectozero

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView.ScaleType
import android.view.ViewGroup
import android.widget.*


/**
 * Created by DrAP on 02/07/2017.
 */

class ImageAdapter(private val mContext: Context) : BaseAdapter() {

    var selectionId = -1

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return mThumbIds[position]
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
        imageView.setImageResource(mThumbIds[position])
        imageView.setOnTouchListener { v, event ->
            selectionId = position
            false
        }
        return imageView
    }

    // references to our images
    private val mThumbIds = arrayOf(
            R.drawable.home,
            R.drawable.dashboard,
            R.drawable.exit,
            R.drawable.gamepad,
            R.drawable.leaderboards_simple,
            R.drawable.massive_multiplayer,
            R.drawable.movie,
            R.drawable.music_on,
            R.drawable.shopping_cart,
            R.drawable.wrench)
}