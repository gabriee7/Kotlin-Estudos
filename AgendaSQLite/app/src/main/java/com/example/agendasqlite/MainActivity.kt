package com.example.agendasqlite

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agendasqlite.databinding.ActivityMainBinding
import com.example.agendasqlite.model.Contato

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<Contato>
    private var pos: Int = -1
    private lateinit var listaContatos: ArrayList<Contato>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAllContatos()

        binding.buttonInserir.setOnClickListener {
            insertUser()
        }

        binding.buttonUpdate.setOnClickListener {
            updateUser()
        }

        binding.buttonDelete.setOnClickListener {
            deleteUser()
        }

        binding.ListView.setOnItemClickListener{ _, _, position, _ ->
            binding.textViewID.setText("ID: ${this.listaContatos[position].id}")
            binding.editNome.setText(this.listaContatos[position].nome)
            binding.editTelefone.setText(this.listaContatos[position].telefone)
            binding.editEmail.setText(this.listaContatos[position].email)
            pos = position
        }
    }

    fun listAllContatos(){
        val db = DBHelper(this)
        this.listaContatos = db.selectAllContato()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaContatos)
        binding.ListView.adapter = adapter
    }

    fun insertUser(){
        val db = DBHelper(this)
        val nome = binding.editNome.text.toString()
        val telefone = binding.editTelefone.text.toString()
        val email = binding.editEmail.text.toString()

        val res = db.insertContato(nome, telefone, email)

        if (res > 0 ){
            listAllContatos()
            adapter.notifyDataSetChanged()
            Toast.makeText(
                applicationContext,
                "Contato cadastrado com sucesso!",
                Toast.LENGTH_SHORT)
                .show()
        }else{
            Toast.makeText(
                applicationContext,
                "Erro ao inserir",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun updateUser(){
        val db = DBHelper(this)

        if(pos >= 0){
            val id = this.listaContatos[pos].id
            val nome = binding.editNome.text.toString()
            val telefone = binding.editTelefone.text.toString()
            val email = binding.editEmail.text.toString()
            val res = db.updateContato(id, nome, telefone, email)

            if(res > 0) {
                Toast.makeText(applicationContext, "Sucesso ao atualizar: $res", Toast.LENGTH_SHORT).show()
                this.listaContatos.set(pos, Contato(id, nome, telefone, email))
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "Erro ao atualizar: $res", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteUser(){
        val db = DBHelper(this)

        if(pos >= 0){
            val id = this.listaContatos[pos].id
            val res = db.deleteContato(id)

            if(res > 0) {
                Toast.makeText(applicationContext, "Sucesso ao excluir: $res", Toast.LENGTH_SHORT).show()
                this.listaContatos.removeAt(pos)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "Erro ao excluir: $res", Toast.LENGTH_SHORT).show()
            }
        }
    }
}