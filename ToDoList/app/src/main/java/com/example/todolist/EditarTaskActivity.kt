package com.example.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityEditarTaskBinding
import com.example.todolist.enums.PrioridadeTaskEnum
import com.example.todolist.enums.StatusTaskEnum
import java.util.Calendar

class EditarTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarTaskBinding
    private lateinit var dbHelper: DBHelper

    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        val id = intent.getIntExtra("id", -1)
        val titulo = intent.getStringExtra("titulo")
        val descricao = intent.getStringExtra("descricao")
        val dataVencimento = intent.getStringExtra("dataVencimento")
        val prioridade = intent.getSerializableExtra("prioridade") as PrioridadeTaskEnum
        val status = intent.getSerializableExtra("status") as StatusTaskEnum

        binding.edtTitulo.setText(titulo)
        binding.edtDescricao.setText(descricao)
        binding.edtDataVencimento.setText(dataVencimento)

        val prioridadeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            PrioridadeTaskEnum.values()
        )
        prioridadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPrioridade.adapter = prioridadeAdapter

        val statusAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            StatusTaskEnum.values()
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = statusAdapter

        binding.spinnerPrioridade.setSelection(prioridade.ordinal)
        binding.spinnerStatus.setSelection(status.ordinal)

        binding.btnAlterar.setOnClickListener {
            val updatedTitulo = binding.edtTitulo.text.toString()
            val updatedDescricao = binding.edtDescricao.text.toString()
            val updatedDataVencimento = binding.edtDataVencimento.text.toString()
            val updatedPrioridade = PrioridadeTaskEnum.values()[binding.spinnerPrioridade.selectedItemPosition]
            val updatedStatus = StatusTaskEnum.values()[binding.spinnerStatus.selectedItemPosition]

            val res = dbHelper.updateTask(id, updatedTitulo, updatedDescricao, updatedDataVencimento, updatedPrioridade, updatedStatus)

            if(res > 0) {
                Toast.makeText(applicationContext, "Sucesso ao atualizar: $res", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Erro ao atualizar: $res", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnExcluir.setOnClickListener {
            val res = dbHelper.deleteTask(id)

            if(res > 0) {
                Toast.makeText(applicationContext, "Sucesso ao tentar excluir: $res", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Erro ao tentar excluir: $res", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-${selectedDay}"

                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.edtDataVencimento.setText(formattedDate)
            },
            year, month, day
        )

        if (selectedDate.isNotEmpty()) {
            val parts = selectedDate.split("-")
            val yearInt = parts[0].toInt()
            val monthInt = parts[1].toInt() - 1
            val dayInt = parts[2].toInt()

            datePickerDialog.updateDate(yearInt, monthInt, dayInt)
        }

        datePickerDialog.show()
    }
}