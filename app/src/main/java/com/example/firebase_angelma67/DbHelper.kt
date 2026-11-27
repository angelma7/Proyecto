package com.example.firebase_angelma67

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, "despensa.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ingredientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ingredientes")
        onCreate(db)
    }

    fun insertarIngrediente(nombre: String) {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
        }
        db.insert("ingredientes", null, valores)
        db.close()
    }

    fun obtenerIngredientes(): List<Pair<Int, String>> {
        val lista = ArrayList<Pair<Int, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre FROM ingredientes ORDER BY id DESC", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            lista.add(Pair(id, nombre))
        }

        cursor.close()
        db.close()
        return lista
    }

    fun eliminarIngrediente(id: Int) {
        val db = writableDatabase
        db.delete("ingredientes", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    fun eliminarIngredientePorNombre(nombre: String) {
        val db = writableDatabase
        db.delete("ingredientes", "nombre = ?", arrayOf(nombre))
        db.close()
    }
}