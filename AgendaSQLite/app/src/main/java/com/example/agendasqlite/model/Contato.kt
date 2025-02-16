package com.example.agendasqlite.model

class Contato (val id: Int = 0, val nome: String = "", val celular: String = "", val email: String = "") {
    override fun toString(): String {
        return "Contato(id=$id, nome='$nome', telefone='$celular', email='$email')"
    }
}