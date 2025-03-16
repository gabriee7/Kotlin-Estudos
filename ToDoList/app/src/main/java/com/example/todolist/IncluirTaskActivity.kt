package com.example.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityIncluirTaskBinding
import com.example.todolist.enums.PrioridadeTaskEnum
import com.example.todolist.enums.StatusTaskEnum
import java.util.Calendar

class IncluirTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncluirTaskBinding
    private lateinit var dbHelper: DBHelper

    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncluirTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

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

        binding.edtDataVencimento.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnSalvar.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString()
            val descricao = binding.edtDescricao.text.toString()
            val prioridade =
                PrioridadeTaskEnum.values()[binding.spinnerPrioridade.selectedItemPosition]
            val status = StatusTaskEnum.values()[binding.spinnerStatus.selectedItemPosition]

            val res = dbHelper.insertTask(titulo, descricao, selectedDate, prioridade, status)

            if(res > 0) {
                Toast.makeText(applicationContext, "Sucesso ao incluir: $res", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Erro ao incluir: $res", Toast.LENGTH_SHORT).show()
            }

            setResult(RESULT_OK)
            finish()
        }

        binding.btnCancelar.setOnClickListener {
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
                binding.edtDataVencimento.text = formattedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}