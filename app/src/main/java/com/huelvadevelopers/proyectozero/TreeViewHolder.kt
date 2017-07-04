package com.huelvadevelopers.proyectozero

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import com.github.johnkil.print.PrintView
import com.huelvadevelopers.proyectozero.model.Category
import com.unnamed.b.atv.model.TreeNode

import com.unnamed.b.atv.model.TreeNode.BaseNodeViewHolder
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import android.view.DragEvent.ACTION_DRAG_ENDED
import android.content.ClipData
import android.view.DragEvent.ACTION_DROP
import android.view.DragEvent.ACTION_DRAG_EXITED
import android.view.DragEvent.ACTION_DRAG_LOCATION
import android.view.DragEvent.ACTION_DRAG_ENTERED
import android.content.ClipDescription
import android.graphics.drawable.ColorDrawable
import android.view.DragEvent
import android.view.DragEvent.ACTION_DRAG_STARTED
import android.view.ViewGroup
import android.widget.LinearLayout


/**
 * Created by DrAP on 25/06/2017.
 */

class TreeViewHolder(context: Context) : TreeNode.BaseNodeViewHolder<Category>(context) {
    private var tvValue: TextView? = null
    private var arrowView: PrintView? = null

    override fun createNodeView(node: TreeNode, value: Category): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_icon_node, null, false)
        tvValue = view.findViewById(R.id.node_value) as TextView
        tvValue!!.text = value.name


        val iconView = view.findViewById(R.id.icon) as PrintView
        //TODO conversion Category.icon al icono correspondiente
        iconView.setImageResource(android.R.drawable.ic_input_get)

        arrowView = view.findViewById(R.id.arrow_icon) as PrintView


        view.findViewById(R.id.btn_addFolder).visibility = View.INVISIBLE
        if(node.level == 2) {
            view.findViewById(R.id.arrow_icon).visibility = View.INVISIBLE
        }

        view.findViewById(R.id.node_value).setOnLongClickListener {
            //Log.v("click", "long click en text")
            val v = (context as Activity).window.decorView.findViewById(android.R.id.content)
            val params = v.findViewById(R.id.treeViewContainer).layoutParams
            (params as LinearLayout.LayoutParams).weight = 90f
            v.findViewById(R.id.treeViewContainer).layoutParams = params
            v.findViewById(R.id.removeCategory).visibility = View.VISIBLE
            var item = ClipData.Item(value.id.toString())

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            var dragData: ClipData = ClipData(value.name, Array(1, { ClipDescription.MIMETYPE_TEXT_PLAIN }), item)
            //Añadimos el numero de subcategorías para despues aceptar el cambio de padre o no
            dragData.addItem(ClipData.Item(node.children.size.toString()))

            // Instantiates the drag shadow builder.
            var myShadow = View.DragShadowBuilder(view)

            // Starts the drag

            it.startDrag(dragData, // the data to be dragged
                    myShadow, // the drag shadow builder
                    null, // no need to use local data
                    0          // flags (not currently used, set to 0)
            )

        }
        if(node.level==1)
            view.findViewById(R.id.node_value).setOnDragListener(MyDragEventListener(value))


        view.findViewById(R.id.btn_delete).visibility = View.INVISIBLE
        /*view.findViewById(R.id.btn_delete).setOnClickListener {
            (context as MainActivity).databaseManager.removeCategory(node.value as Category)
            treeView.removeNode(node)
        }*/

        view.setPadding( ( node.level - 1 ) * 100, 0, 0, 0)
        return view
    }

    override fun toggle(active: Boolean) {
        arrowView!!.iconText = context.resources.getString(if (active) R.string.ic_keyboard_arrow_down else R.string.ic_keyboard_arrow_right)
    }

    inner class MyDragEventListener(category : Category) : View.OnDragListener {

        val category = category
        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        override fun onDrag(v: View, event: DragEvent): Boolean {

            // Defines a variable to store the action type for the incoming event
            val action = event.getAction()
            // Handles each of the expected events
            when (action) {

                DragEvent.ACTION_DRAG_STARTED -> {

                    // Determines if this View can accept the dragged data
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // returns true to indicate that the View can accept the dragged data.
                        return true
                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return false
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.setBackgroundColor(Color.GRAY)
                    return true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    return true

                DragEvent.ACTION_DRAG_EXITED -> {
                    v.setBackgroundColor(android.R.color.background_light)
                    return true
                }

                DragEvent.ACTION_DROP -> {

                    // Obtenemos el id de la categoria que hizo drag
                    val dragCategoryId : String = event.clipData.getItemAt(0).text as String
                    //Obtenemos el numero de subcategorias
                    val nChildren = (event.clipData.getItemAt(1).text as String).toInt()

                    // Displays a message containing the dragged data.
                    //Toast.makeText(context, "Dragged " + dragData + " into " + category.name, Toast.LENGTH_LONG).show()
                    Log.v("drag",  "Dragged " + dragCategoryId + " into " + category.name)
                    if(nChildren == 0) {
                        //TODO dialogo de error
                        (context as MainActivity).databaseManager.changeCategoryParent(category.id, dragCategoryId.toInt())
                        (context as MainActivity).goSection(3)
                    }
                    v.setBackgroundColor(android.R.color.background_light)

                    // Returns true. DragEvent.getResult() will return true.
                    return true
                }

                DragEvent.ACTION_DRAG_ENDED -> {

                    v.setBackgroundColor(android.R.color.background_light)
                    val v = (context as Activity).window.decorView.findViewById(android.R.id.content)
                    val params = v.findViewById(R.id.treeViewContainer).layoutParams
                    (params as LinearLayout.LayoutParams).weight=100f
                    v.findViewById(R.id.treeViewContainer).layoutParams = params
                    v.findViewById(R.id.removeCategory).visibility=View.GONE

                    // Does a getResult(), and displays what happened.
                    if (event.getResult()) {
                        //Toast.makeText(context, "The drop was handled.", Toast.LENGTH_LONG).show()
                        Log.v("drag",  "The drop was handled.")

                    } else {
                        //Toast.makeText(context, "The drop didn't work.", Toast.LENGTH_LONG).show()
                        Log.v("drag",  "The drop didn't work.")
                    }

                    // returns true; the value is ignored.
                    return true
                }

            // An unknown action type was received.
                else -> Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
            }

            return false
        }
    };
}