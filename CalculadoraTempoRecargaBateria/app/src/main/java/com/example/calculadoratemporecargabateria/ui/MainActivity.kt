package com.example.calculadoratemporecargabateria.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoratemporecargabateria.databinding.ActivityMainBinding
import com.example.calculadoratemporecargabateria.databinding.ModalLayoutBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalcular.setOnClickListener {

            val capacidadeBateria = binding.editTextCapacidadeBateria.text.toString()
            val cargaAtual = binding.editTextCargaAtualBateria.text.toString()
            val correnteCarregamento = binding.editTextCorrenteCarregamento.text.toString()
            val tensaoEntrada = binding.editTextTensaoEntrada.text.toString()

            if (capacidadeBateria.isEmpty() ||
                cargaAtual.isEmpty() ||
                correnteCarregamento.isEmpty() ||
                tensaoEntrada.isEmpty()
                ){
                Toast.makeText(this, "Há campos vazios, insira os dados corretamente!", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            val capacidadeBateriaDouble = capacidadeBateria.toDoubleOrNull()
            val cargaAtualDouble = cargaAtual.toDoubleOrNull()
            val correnteCarregamentoDouble = correnteCarregamento.toDoubleOrNull()
            val tensaoEntradaDouble = tensaoEntrada.toDoubleOrNull()

            if (capacidadeBateriaDouble == null || cargaAtualDouble == null ||
                correnteCarregamentoDouble == null || tensaoEntradaDouble == null) {
                Toast.makeText(this, "Valores inválidos, insira números válidos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = calcularTempoDeCarregamento(capacidadeBateria.toDouble(),
                cargaAtual.toDouble(),
                correnteCarregamento.toDouble(),
                tensaoEntrada.toDouble(),
            )

            abrirModal("Resultado", result)
        }
    }

    private fun calcularTempoDeCarregamento(
            capacidadeBateria: Double,
            cargaAtual: Double,
            correnteCarregamento: Double,
            tensaoEntrada: Double
        ): String {

        val potenciaNominal = (tensaoEntrada * correnteCarregamento) / 1000

        val aCarregar = ((100 - cargaAtual) * capacidadeBateria) / 100

        val tempoCarregamento = aCarregar / potenciaNominal

        return """
        Potência Nominal (PN): ${"%.2f".format(potenciaNominal)} kW
        Energia a Carregar (AC): ${"%.2f".format(aCarregar)} kW
        Tempo Necessário de Carregamento: ${"%.2f".format(tempoCarregamento)} horas
    """.trimIndent()
    }

    private fun abrirModal(titulo: String, mensagem: String) {
        val dialog = Dialog(this)

        val modalBinding = ModalLayoutBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(modalBinding.root)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val modalWidth = (screenWidth * 0.9).toInt()

        dialog.window?.setLayout(modalWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        modalBinding.modalTitle.text = titulo
        modalBinding.modalMessage.text = mensagem

        modalBinding.closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
