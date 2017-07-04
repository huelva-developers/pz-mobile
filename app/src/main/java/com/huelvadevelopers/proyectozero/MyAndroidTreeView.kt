package com.huelvadevelopers.proyectozero

import android.content.Context
import android.view.DragEvent
import android.view.View
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView

/**
 * Created by DrAP on 01/07/2017.
 */

class MyAndroidTreeView(context: Context, root : TreeNode) : AndroidTreeView(context, root) {

    val mContext = context

    override fun getView(): View {
        val view = super.getView()
        view.findViewById(R.id.tree_items).setPadding(20, 20, 20, 250)
        view.setOnDragListener { v, event ->
            if(event.action== DragEvent.ACTION_DROP){
                val item = event.clipData.getItemAt(0)
                var dragData : String = item.text as String
                (mContext as MainActivity).databaseManager.changeCategoryParent(0,dragData.toInt())
                mContext.goSection(3)
            }
            true
        }
        return view
    }
}