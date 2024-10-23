package com.example.applistatelefonica.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.applistatelefonica.model.UserModel

class DBHemper(context: Context): SQLiteOpenHelper( context,"database.db", null,1 ){

    val sql = arrayOf(
        "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT",
        "INSERT INTO users (username, password) VALUES ('admin','password')"
    )

    override fun onCreate(db: SQLiteDatabase?) {

        sql.forEach { db?.execSQL(it) }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertUser(userName: String, passWord: String): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", userName)
        contentValues.put("password", passWord)
        val res = db.insert("users",null, contentValues)
        db.close()
        return res
    }

    fun updateUser(id: Int, userName: String, passWord: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", userName)
        contentValues.put("password", passWord)
        val res = db.update("users",contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res

    }

    fun deleteUser(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("users", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getUser(userName: String, passWord: String): UserModel{
        val db = this.writableDatabase
        val c = db.rawQuery("SELECT * FROM users WHERE username =? AND password=?", arrayOf(userName, passWord))
        var userModel = UserModel()
        if(c.count == 1){
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("username")
            val passWordIndex = c.getColumnIndex("password")

            userModel = UserModel(id = c.getInt(idIndex),
                userName = c.getString(nameIndex),
                passWord = c.getString(passWordIndex)
            )
        }
        db.close()
        return userModel
    }


    fun login(userName: String, passWord: String): Boolean {
        val db = this.writableDatabase
        val c = db.rawQuery(
            "SELECT * FROM users WHERE username =? AND password=?",
            arrayOf(userName, passWord)
        )

        if (c.count == 1) {
            db.close()
            return true
        }else{
        db.close()
        return false
        }
    }
}