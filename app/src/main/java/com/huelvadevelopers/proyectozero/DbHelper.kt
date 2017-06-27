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

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val DB_NAME = "pz.sqlite"
        private val DB_SHEME_VERSION = 1
    }
}
