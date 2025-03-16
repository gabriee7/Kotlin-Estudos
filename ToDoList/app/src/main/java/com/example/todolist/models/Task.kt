package com.example.todolist.models

import com.example.todolist.enums.PrioridadeTaskEnum
import com.example.todolist.enums.StatusTaskEnum

class Task (
    val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val dataVencimento: String,
    val prioridade: PrioridadeTaskEnum,
    val status: StatusTaskEnum) {

    override fun toString(): String {
        return "Task(id=$id, titulo='$titulo', descricao='$descricao', dataVencimento='$dataVencimento', prioridade=${prioridade.toString()}, status=${status.toString()})"
    }

}