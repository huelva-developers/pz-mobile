package com.huelvadevelopers.proyectozero

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.huelvadevelopers.proyectozero.model.Category

/**
 * Created by DrAP on 04/07/2017.
 */

class CategoryDialog(context : Context) : Builder( context ){

    val mContex = context
    lateinit var mAlertDialog : AlertDialog
    lateinit var edt : EditText
    lateinit var spinner : Spinner
    lateinit var gridView : GridView
    var categoryParent = ArrayList<Category>()
    var currentCategory : Category? = null // Solo se usa para editar categories


    override fun show(): AlertDialog {
        val dialogView = LayoutInflater.from(mContex).inflate(R.layout.add_category_dialog, null)
        setView(dialogView)

        //Name EditText
        edt = dialogView.findViewById(R.id.add_category_name) as EditText
        //Parent Spinner
        spinner = dialogView.findViewById(R.id.add_category_parent) as Spinner
        val categories = (mContex as MainActivity).databaseManager.getCategories()
        val nameParents = ArrayList<String>()
        nameParents.add("None")
        categoryParent = ArrayList<Category>()
        for ( c : Category in categories ){
            if(c.parent==null){
                nameParents.add(c.name)
                categoryParent.add(c)
            }
        }
        val adapter : ArrayAdapter<String> = ArrayAdapter(mContex, android.R.layout.simple_spinner_item, nameParents.toTypedArray())
        spinner.adapter = adapter
        //Icons GridView
        gridView = dialogView.findViewById(R.id.add_category_icon) as GridView
        gridView.adapter = ImageAdapter(mContex)
        (gridView.adapter as ImageAdapter).selectionId=0

        setTitle(mContex.getString(R.string.sNewCategory))
        setPositiveButton("Done") { dialog, whichButton ->
            //Vacio porque abajo lo sobrescribimos para decidir si hacer dismiss o no
        }
        setNegativeButton("Cancel") { dialog, whichButton ->
            //pass
        }
        if(currentCategory != null){ //Si currentCategory no es nulo es porque estamos editanto
            edt.setText(currentCategory!!.name)
            if(currentCategory!!.children.size>0)
                dialogView.findViewById(R.id.add_category_parent_layout).visibility = View.GONE
            else {
                if(currentCategory!!.parent==null)
                    spinner.setSelection(0)
                else {
                    for (c: Category in categoryParent) {
                        if (c.id == currentCategory!!.parent!!.id)
                            spinner.setSelection(categoryParent.indexOf(c)+1)
                    }
                }
            }
            (gridView.adapter as ImageAdapter).selectionId = currentCategory!!.icon
            (gridView.adapter as ImageAdapter).notifyDataSetChanged()
            setTitle(context.getString(R.string.sEditCategory))
        }
        mAlertDialog = super.show()
        return mAlertDialog
    }
}