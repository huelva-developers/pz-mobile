package com.huelvadevelopers.proyectozero

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.github.johnkil.print.PrintView
import com.unnamed.b.atv.model.TreeNode

import com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder



/**
 * Created by DrAP on 25/06/2017.
 */

class TreeViewHolder(context: Context) : TreeNode.BaseNodeViewHolder<TreeViewHolder.IconTreeItem>(context) {
    private var tvValue: TextView? = null
    private var arrowView: PrintView? = null

    override fun createNodeView(node: TreeNode, value: IconTreeItem): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_icon_node, null, false)
        tvValue = view.findViewById(R.id.node_value) as TextView
        tvValue!!.text = value.text


        val iconView = view.findViewById(R.id.icon) as PrintView
        iconView.setImageResource(value.icon)

        arrowView = view.findViewById(R.id.arrow_icon) as PrintView

        if(node.level == 2) {
            view.findViewById(R.id.arrow_icon).visibility = View.INVISIBLE
            view.findViewById(R.id.btn_addFolder).visibility = View.INVISIBLE
        }

        view.findViewById(R.id.icon).setOnClickListener {
            Log.v("click", "click en icon")
        }

        view.findViewById(R.id.node_value).setOnClickListener {
            Log.v("click", "click en text")
        }

        view.findViewById(R.id.arrow_icon).setOnClickListener {
            treeView.toggleNode(node)
            Log.v("click", "click en arrow")
        }
        view.setOnClickListener {
            Log.v("click", "click en view")
        }



        view.findViewById(R.id.btn_addFolder).setOnClickListener {
            val newFolder = TreeNode(TreeViewHolder.IconTreeItem(android.R.drawable.ic_input_get, "New Folder"))
            treeView.addNode(node, newFolder)
            treeView.expandNode(node)
            Log.v("click", "click en new folder")
        }

        view.findViewById(R.id.btn_delete).setOnClickListener { treeView.removeNode(node) }

        /*//if My computer
        if (node.getLevel() === 1) {
            view.findViewById(R.id.btn_delete).visibility = View.GONE
        }*/
        view.setPadding( ( node.level - 1 ) * 100, 0, 0, 0)
        //view.setBackgroundColor(Color.argb(150,255,0,0))
        return view
    }

    override fun toggle(active: Boolean) {
        arrowView!!.iconText = context.resources.getString(if (active) R.string.ic_keyboard_arrow_down else R.string.ic_keyboard_arrow_right)
    }

    class IconTreeItem(icon: Int, text: String){
        val icon = icon
        var text = text
    }
}