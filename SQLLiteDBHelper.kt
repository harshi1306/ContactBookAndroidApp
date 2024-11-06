package com.example.ca2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLLiteDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, "
                + NAME_COL + " TEXT,"
                + PHONE_COL + " TEXT" + ")")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(name: String, phone: String) {
        val values = ContentValues().apply {
            put(NAME_COL, name)
            put(PHONE_COL, phone)
        }
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL=?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllContacts(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    companion object {
        const val DATABASE_NAME = "ContactDB"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Contacts"
        const val ID_COL = "ContactID"
        const val NAME_COL = "ContactName"
        const val PHONE_COL = "PhoneNumber"
    }
}
