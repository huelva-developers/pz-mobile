package com.huelvadevelopers.proyectozero

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Environment
import android.util.Log
import com.huelvadevelopers.proyectozero.model.Category

import java.io.File
import java.util.Locale
import java.util.Vector

/**
 * Created by DrAP on 23/6/2017.
 */
class DataBaseManager(context: Context) {
    var FIL_DIR: Array<File>? = null
    lateinit var db : SQLiteDatabase
    private val helper: DbHelper

    init {
        helper = DbHelper(context)
        val dbfile = File(Environment.getExternalStorageDirectory().path + "/databases/pz.sqlite")
        try {
            //db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            db = helper.writableDatabase
            db!!.execSQL("PRAGMA foreign_keys = ON;")
        } catch (e: android.database.sqlite.SQLiteCantOpenDatabaseException) {
            Log.e("DB no se abrio", e.toString())
        }

        //db=helper.getReadableDatabase();
    }

    fun getCategories(): Vector<Category> {
        val query = "select * from category order by parent_id ASC"
        val cursor = db!!.rawQuery(query, null)
        val v = Vector<Category>()
        while (cursor.moveToNext()) {
            val category= Category(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getInt(4))
            val parentId =  if (cursor.isNull(1)) -1 else cursor.getInt(1)
            if(parentId != -1)
                Log.v("parentId", "Id: "+category.id+", parent: $parentId")
                for(c: Category in v){
                    if(c.id == parentId) {
                        c.addChild(category)
                        break
                    }
                }
            v.add(category)
        }
        return v
    }

    fun removeCategory( category : Category) {
        var query = "delete from category where id = "+category.id
        db!!.execSQL(query)
    }

    fun removeCategoryById( id : Int) {
        var query = "delete from category where id = "+id
        db!!.execSQL(query)
    }

    fun addCategory( category : Category) {
        val cv = ContentValues()
        if(category.parent!=null)
            cv.put("parent_id", category.parent?.id)
        cv.put("name", category.name)
        cv.put("icon", category.icon)
        cv.put("type", category.type)

        db!!.insert("category",null,cv)
        Log.v("Categoria insertada", category.toString())
    }

    fun changeCategoryParent( parentId : Int, childrenId : Int){
        val query = "update category set parent_id = $parentId where id = $childrenId"
        db!!.execSQL(query)
    }
}