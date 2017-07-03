package com.huelvadevelopers.proyectozero

import android.content.Context
import android.view.View
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView

/**
 * Created by DrAP on 01/07/2017.
 */

class MyAndroidTreeView(context: Context, root : TreeNode) : AndroidTreeView(context, root) {

    override fun getView(): View {
        val view = super.getView()
        view.findViewById(R.id.tree_items).setPadding(20, 20, 20, 250)
        return view
    }
}