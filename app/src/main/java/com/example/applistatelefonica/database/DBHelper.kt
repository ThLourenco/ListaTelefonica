package com.example.applistatelefonica.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.applistatelefonica.model.ContactModel
import com.example.applistatelefonica.model.UserModel

class DBHelper(context: Context): SQLiteOpenHelper( context,"database.db", null,1 ){

    val sql = arrayOf(
        "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)",
        "INSERT INTO users (username, password) VALUES ('admin','password')",
        "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, email TEXT, phone INTEGER, imageid INTEGER)",
        "INSERT INTO contact (name, address, email, phone, imageid) VALUES ('maria', 'rua2', 'ma@g','331122', '1')",
        "INSERT INTO contact (name, address, email, phone, imageid) VALUES ('joao', 'rua3', 'Jo@g', '331122', '2')"
    )

    //val  id: Int =0,
    //    val name: String = "",
    //    val address: String = "",
    //    val email: String = "",
    //    val phone: Int = 0,
    //    val imageId: Int = 0

    override fun onCreate(db: SQLiteDatabase?) {

        sql.forEach { db?.execSQL(it) }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    /*-----------------------------------------------------------------
                CRUD USERS TABLE
     -----------------------------------------------------------------*/
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

    /*-----------------------------------------------------------------
            CRUD CONTACT TABLE
 -----------------------------------------------------------------*/

    fun insertContact(name: String, address: String, email: String, phone: Int,imageId: Int): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("address", address)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageid", imageId)
        val res = db.insert("contact",null, contentValues)
        db.close()
        return res
    }

    fun updateContact(id: Int, name: String, address: String, email: String, phone: Int,imageId: Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("address", address)
        contentValues.put("email", email)
        contentValues.put("phone", phone)
        contentValues.put("imageid", imageId)
        val res = db.update("contact",contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun deleteContact(id: Int): Int{
        val db = this.writableDatabase
        val res = db.delete("contact", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun getContact(id: Int): ContactModel{
        val db = this.writableDatabase
        val c = db.rawQuery("SELECT * FROM contact WHERE id=?", arrayOf(id.toString()))
        var contactModel = ContactModel()
        if(c.count == 1){
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imagemId = c.getColumnIndex("imageid")

            contactModel = ContactModel(id = c.getInt(idIndex),
                name = c.getString(nameIndex),
                email = c.getString(emailIndex),
                phone = c.getInt(phoneIndex),
                imageId = c.getInt(imagemId)
            )
        }
        db.close()
        return contactModel
    }

    fun getAllContact(): ArrayList<ContactModel>{
        val db = this.writableDatabase
        val c = db.rawQuery("SELECT * FROM contact",null)
        var listContactModel = ArrayList<ContactModel>()
        if(c.count > 0) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val nameIndex = c.getColumnIndex("name")
            val emailIndex = c.getColumnIndex("email")
            val phoneIndex = c.getColumnIndex("phone")
            val imagemId = c.getColumnIndex("imageid")
            do {


               val contactModel = ContactModel(
                    id = c.getInt(idIndex),
                    name = c.getString(nameIndex),
                    email = c.getString(emailIndex),
                    phone = c.getInt(phoneIndex),
                    imageId = c.getInt(imagemId)
                )
                listContactModel.add(contactModel)
            }while (c.moveToNext())
        }
        db.close()
        return listContactModel
    }


}