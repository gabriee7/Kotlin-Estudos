package com.example.todolist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ActivityTarefasConcluidasBinding

class TarefasConcluidasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTarefasConcluidasBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarefasConcluidasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        val tarefasConcluidasTextView = binding.tarefasConcluidasTextView

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        val tarefasConcluidas = dbHelper.getTarefasConcluidasOrdenadas()

        var tarefasText = ""
        for (tarefa in tarefasConcluidas) {
            tarefasText += "Título: ${tarefa.titulo}\nDescrição: ${tarefa.descricao}\nData de Vencimento: ${tarefa.dataVencimento}\n\n"
        }

        tarefasConcluidasTextView.text = tarefasText

    }
}
