package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.enums.PrioridadeTaskEnum
import com.example.todolist.enums.StatusTaskEnum
import com.example.todolist.models.Task

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<Task>
    private lateinit var listaTasks: ArrayList<Task>
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        listAllTasks()

        binding.ListView.setOnItemClickListener { _, _, position, _ ->
            val task = listaTasks[position]

            val intent = Intent(this, EditarTaskActivity::class.java)

            intent.putExtra("id", task.id)
            intent.putExtra("titulo", task.titulo)
            intent.putExtra("descricao", task.descricao)
            intent.putExtra("dataVencimento", task.dataVencimento)
            intent.putExtra("prioridade", task.prioridade)
            intent.putExtra("status", task.status)

            startActivityForResult(intent, REQUEST_CODE_EDITAR_TASK)
        }

        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, IncluirTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADICIONAR_TASK)
        }

        binding.btnConcluidas.setOnClickListener {
            val intent = Intent(this, TarefasConcluidasActivity::class.java)
            startActivity(intent)
        }
    }

    fun listAllTasks() {
        listaTasks = dbHelper.selectAllTasks()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTasks)
        binding.ListView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDITAR_TASK && resultCode == RESULT_OK ||
            requestCode == REQUEST_CODE_ADICIONAR_TASK && resultCode == RESULT_OK) {
            listAllTasks()
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val REQUEST_CODE_EDITAR_TASK = 1001
        const val REQUEST_CODE_ADICIONAR_TASK = 1002
    }
}
