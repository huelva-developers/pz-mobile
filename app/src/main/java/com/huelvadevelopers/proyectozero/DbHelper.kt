package com.huelvadevelopers.proyectozero

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by DrAP on 23/06/2017.
 */
class DbHelper(context: Context)
    : SQLiteOpenHelper(context, DbHelper.DB_NAME, null, DbHelper.DB_SHEME_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE category ("+
                "id INTEGER PRIMARY KEY  AUTOINCREMENT, "+
                "parent_id INT, "+
                "name VARCHAR NOT NULL, "+
                "icon INT NOT NULL, "+
                "type INT NOT NULL, "+
                "FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE)")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val DB_NAME = "pz.sqlite"
        private val DB_SHEME_VERSION = 1
    }
}
