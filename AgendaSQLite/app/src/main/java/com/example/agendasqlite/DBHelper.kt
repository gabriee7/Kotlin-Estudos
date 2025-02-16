package com.example.agendasqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.agendasqlite.model.Contato

class DBHelper(context: Context):
    SQLiteOpenHelper(context, "database.db", null, 1) {
        var sql = arrayOf(
            "CREATE TABLE contatos (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT, celular TEXT, email TEXT",
            "INSERT INTO contatos (nome, celular, email) VALUES ('Zé da mercearia', '28 00000-0000', 'zezinho@agenda.com')",
            "INSERT INTO contatos (nome, celular, email) VALUES ('Emergência', '192', 'emergencia@agenda.com')")

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE contatos")
        onCreate(db)
    }

    fun selectAllContato(): ArrayList<Contato> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM contatos", null)
        val list: ArrayList<Contato> = ArrayList()

        if (cursor.count > 0 ){
            cursor.moveToFirst()

            do {
                val idIndex = cursor.getColumnIndex("id")
                val nomeIndex = cursor.getColumnIndex("nome")
                val telefoneIndex = cursor.getColumnIndex("telefone")
                val emailIndex = cursor.getColumnIndex("email")

                val id = cursor.getInt(idIndex)
                val nome = cursor.getString(nomeIndex)
                val telefone = cursor.getString(telefoneIndex)
                val email = cursor.getString(emailIndex)

                list.add(Contato(id, nome, telefone, email))
            }while (cursor.moveToNext())
        }

        db.close()
        return list
    }

    fun insertContato(nome: String, telefone: String, email: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("nome", nome)
        contentValues.put("telefone", telefone)
        contentValues.put("email", email)

        val res = db.insert("contatos", null, contentValues)
        db.close()

        return res
    }

    fun updateContato(id: Int, nome: String, telefone: String, email: String): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put("nome", nome)
        contentValues.put("telefone", telefone)
        contentValues.put("email", email)

        val res = db.update("contatos", contentValues, "id=?", arrayOf(id.toString()))
        db.close()

        return res
    }

    fun deleteContato(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("contatos", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }
}