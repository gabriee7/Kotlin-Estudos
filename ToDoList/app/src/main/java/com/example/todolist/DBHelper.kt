package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.enums.PrioridadeTaskEnum
import com.example.todolist.enums.StatusTaskEnum
import com.example.todolist.models.Task

class DBHelper(context: Context):
    SQLiteOpenHelper(context, "database.db", null, 1) {
    val sql = arrayOf(
        "CREATE TABLE tarefas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, " +
                "descricao TEXT, " +
                "dataVencimento TEXT, " +
                "prioridade INTEGER, " +
                "status INTEGER" +
                ")",

        "INSERT INTO tarefas (" +
                "titulo, " +
                "descricao, " +
                "dataVencimento, " +
                "prioridade, " +
                "status" +
                ") VALUES (" +
                "'Título de exemplo', " +
                "'Descrição de exemplo', " +
                "'2025-03-15', " +
                "${PrioridadeTaskEnum.BAIXA.ordinal}, " +
                "${StatusTaskEnum.ANDAMENTO.ordinal}" +
                ")"
    )

        override fun onCreate(db: SQLiteDatabase) {
            sql.forEach {
                db.execSQL(it)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS tarefas")
            onCreate(db)
        }

    fun selectAllTasks(): ArrayList<Task> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tarefas", null)
        val list: ArrayList<Task> = ArrayList()

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val idIndex = cursor.getColumnIndex("id")
                val tituloIndex = cursor.getColumnIndex("titulo")
                val descricaoIndex = cursor.getColumnIndex("descricao")
                val dataVencimentoIndex = cursor.getColumnIndex("dataVencimento")
                val prioridadeIndex = cursor.getColumnIndex("prioridade")
                val statusIndex = cursor.getColumnIndex("status")

                val id = cursor.getInt(idIndex)
                val titulo = cursor.getString(tituloIndex)
                val descricao = cursor.getString(descricaoIndex)
                val dataVencimento = cursor.getString(dataVencimentoIndex)
                val prioridade = cursor.getInt(prioridadeIndex)
                val status = cursor.getInt(statusIndex)

                val prioridadeEnum = PrioridadeTaskEnum.values().firstOrNull { it.codigo == prioridade }
                    ?: throw IllegalArgumentException("Prioridade inválida: $prioridade")
                val statusEnum = StatusTaskEnum.values().firstOrNull { it.codigo == status }
                    ?: throw IllegalArgumentException("Status inválido: $status")

                list.add(Task(id, titulo, descricao, dataVencimento, prioridadeEnum, statusEnum))
            } while (cursor.moveToNext())
        }

        db.close()
        return list
    }


        fun insertTask(titulo: String, descricao: String, dataVencimento: String, prioridade: PrioridadeTaskEnum, status: StatusTaskEnum): Long {
            val db = this.writableDatabase
            val contentValues = ContentValues()

            contentValues.put("titulo", titulo)
            contentValues.put("descricao", descricao)
            contentValues.put("dataVencimento", dataVencimento)
            contentValues.put("prioridade", prioridade.ordinal)
            contentValues.put("status", status.ordinal)

            val res = db.insert("tarefas", null, contentValues)
            db.close()

            return res
        }

        fun updateTask(id: Int, titulo: String, descricao: String, dataVencimento: String, prioridade: PrioridadeTaskEnum, status: StatusTaskEnum): Int{
            val db = this.writableDatabase
            val contentValues = ContentValues()

            contentValues.put("titulo", titulo)
            contentValues.put("descricao", descricao)
            contentValues.put("dataVencimento", dataVencimento)
            contentValues.put("prioridade", prioridade.ordinal)
            contentValues.put("status", status.ordinal)

            val res = db.update("tarefas", contentValues, "id=?", arrayOf(id.toString()))
            db.close()

            return res
        }

        fun deleteTask(id: Int): Int {
            val db = this.writableDatabase
            val res = db.delete("tarefas", "id=?", arrayOf(id.toString()))
            db.close()
            return res
        }

    fun getTarefasConcluidasOrdenadas(): List<Task> {
        val tarefasConcluidas = mutableListOf<Task>()

        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM Tarefas WHERE status = ? ORDER BY dataVencimento DESC",
            arrayOf(StatusTaskEnum.CONCLUIDA.ordinal.toString())
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex("id")
                val tituloIndex = cursor.getColumnIndex("titulo")
                val descricaoIndex = cursor.getColumnIndex("descricao")
                val dataVencimentoIndex = cursor.getColumnIndex("dataVencimento")
                val prioridadeIndex = cursor.getColumnIndex("prioridade")
                val statusIndex = cursor.getColumnIndex("status")

                val id = cursor.getInt(idIndex)
                val titulo = cursor.getString(tituloIndex)
                val descricao = cursor.getString(descricaoIndex)
                val dataVencimento = cursor.getString(dataVencimentoIndex)
                val prioridade = cursor.getInt(prioridadeIndex)
                val status = cursor.getInt(statusIndex)

                val prioridadeEnum = PrioridadeTaskEnum.values().firstOrNull { it.codigo == prioridade }
                    ?: throw IllegalArgumentException("Prioridade inválida: $prioridade")
                val statusEnum = StatusTaskEnum.values().firstOrNull { it.codigo == status }
                    ?: throw IllegalArgumentException("Status inválido: $status")

                val task = Task(id, titulo, descricao, dataVencimento, prioridadeEnum, statusEnum)
                tarefasConcluidas.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return tarefasConcluidas
    }
}