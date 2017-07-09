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

        db.execSQL("CREATE TABLE bank_account (id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "name VARCHAR NOT NULL ,description VARCHAR NOT NULL ,balance FLOAT NOT NULL ,"+
                "currency VARCHAR NOT NULL , color INT NOT NULL, "+
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ,"+
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL )")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion == 2)
            db.execSQL("DROP TABLE bank_account")
        if(oldVersion==1 || oldVersion==2)
            db.execSQL("CREATE TABLE bank_account (id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                    "name VARCHAR NOT NULL ,description VARCHAR NOT NULL ,balance FLOAT NOT NULL ,"+
                    "currency VARCHAR NOT NULL , color INT NOT NULL, "+
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ,"+
                    "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL )")
    }

    companion object {
        private val DB_NAME = "pz.sqlite"
        private val DB_SHEME_VERSION = 3
    }
}
