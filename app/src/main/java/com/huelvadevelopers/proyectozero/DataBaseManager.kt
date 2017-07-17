package com.huelvadevelopers.proyectozero

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Environment
import android.util.Log
import com.huelvadevelopers.proyectozero.model.BankAccount
import com.huelvadevelopers.proyectozero.model.Category
import com.huelvadevelopers.proyectozero.model.Transaction

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
    fun getCategoriesName(): ArrayList<String> {
        val query = "select name from category order by parent_id ASC"
        val cursor = db!!.rawQuery(query, null)
        val v = ArrayList<String>()
        while (cursor.moveToNext()) {
            v.add(cursor.getString(0))
        }
        return v
    }
    fun getCategoryByName(n : String): Category? {
        val v = getCategories()
        for (category : Category in v){
            if (category.name.equals(n))
                return category
        }
        return null
    }
    fun getCategoryById(id : Int): Category? {
        val v = getCategories()
        for (category : Category in v){
            if (category.id == id)
                return category
        }
        return null
    }

    fun removeCategory( category : Category) {
        var query = "delete from category where id = "+category.id
        db!!.execSQL(query)
    }

    fun removeCategoryById( id : Int) {
        var query = "delete from category where id = "+id
        db!!.execSQL(query)
    }

    fun editCategory( category: Category) {
        val cv = ContentValues()
        cv.put("id",category.id)
        if(category.parent!=null)
            cv.put("parent_id", category.parent?.id)
        cv.put("name", category.name)
        cv.put("icon", category.icon)
        cv.put("type", category.type)

        db!!.replace("category",null,cv)
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
        val query : String
        if(parentId != 0)
            query = "update category set parent_id = $parentId where id = $childrenId"
        else
            query = "update category set parent_id = null where id = $childrenId"
        db!!.execSQL(query)
    }

    fun addBankAccount( account : BankAccount) {
        val cv = ContentValues()
        cv.put("name", account.name)
        cv.put("description", account.description)
        cv.put("balance", account.balance)
        cv.put("currency", account.currency)
        cv.put("color", account.color)

        db!!.insert("bank_account",null,cv)
    }

    fun getBankAccounts(): ArrayList<BankAccount> {
        val query = "select * from bank_account"
        val cursor = db!!.rawQuery(query, null)
        val v = ArrayList<BankAccount>()
        while (cursor.moveToNext()) {
            val account= BankAccount(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getString(4),
                    cursor.getInt(5))
            v.add(account)
        }
        return v
    }
    fun getBankAccountsNames(): ArrayList<String> {
        val query = "select name from bank_account"
        val cursor = db!!.rawQuery(query, null)
        val v = ArrayList<String>()
        while (cursor.moveToNext()) {
            v.add(cursor.getString(0))
        }
        return v
    }
    fun getBankAccountByName(n : String): BankAccount? {
        val v = getBankAccounts()
        for (account : BankAccount in v){
            if (account.name.equals(n))
                return account
        }
        return null
    }
    fun getBankAccountById(id : Int): BankAccount? {
        val v = getBankAccounts()
        for (account : BankAccount in v){
            if (account.id == id)
                return account
        }
        return null
    }

    fun removeBankAccountById( id : Int) {
        var query = "delete from bank_account where id = "+id
        db!!.execSQL(query)
    }

    fun addTransaction( transaction : Transaction) {
        val cv = ContentValues()
        cv.put("bank_account", transaction.bankAccount.id)
        if(transaction.category != null)
            cv.put("category", transaction.category.id)
        cv.put("description", transaction.description)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        cv.put("date", dateFormat.format(transaction.date))
        cv.put("amount", transaction.amount)

        db!!.insert("'transaction'",null,cv)
    }
    fun getTransactions(): ArrayList<Transaction> {
        val query = "select * from 'transaction' order by date DESC"
        val cursor = db!!.rawQuery(query, null)
        val v = ArrayList<Transaction>()
        while (cursor.moveToNext()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date : Date = dateFormat.parse(cursor.getString(4))
            val transaction= Transaction(cursor.getInt(0), getBankAccountById(cursor.getInt(1))!!, getCategoryById(cursor.getInt(2))!!,
                    cursor.getString(3), date, cursor.getDouble(5))
            v.add(transaction)
        }
        return v
    }
    fun getTransactions(limit : Int): ArrayList<Transaction> {
        val query = "select * from 'transaction' order by date DESC LIMIT $limit"
        val cursor = db!!.rawQuery(query, null)
        val v = ArrayList<Transaction>()
        while (cursor.moveToNext()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var date : Date = dateFormat.parse(cursor.getString(4))
            val transaction= Transaction(cursor.getInt(0), getBankAccountById(cursor.getInt(1))!!, getCategoryById(cursor.getInt(2))!!,
                    cursor.getString(3), date, cursor.getDouble(5))
            v.add(transaction)
        }
        return v
    }

    fun removeTransactionById( id : Int) {
        var query = "delete from 'transaction' where id = "+id
        db!!.execSQL(query)
    }

    fun createRandomTransactions(){
        var ran = Random(System.currentTimeMillis())
        val accounts = getBankAccounts()
        val categories = getCategories()
        repeat(200) { i ->
            var cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, ran.nextInt(28))
            cal.set(Calendar.YEAR, ran.nextInt(2)+2016)
            cal.set(Calendar.MONTH, ran.nextInt(12))
            cal.set(Calendar.HOUR_OF_DAY, ran.nextInt(24))
            cal.set(Calendar.MINUTE, ran.nextInt(60))
            cal.set(Calendar.SECOND, ran.nextInt(60))
            var amount = ran.nextInt(1000)
            if(ran.nextBoolean()) amount *= -1
            val t = Transaction(1000+i, accounts[ran.nextInt(accounts.size)], categories[ran.nextInt(categories.size)],
                    "Transacción random numero "+i, cal.time, amount.toDouble())
            addTransaction(t)
        }
    }
}