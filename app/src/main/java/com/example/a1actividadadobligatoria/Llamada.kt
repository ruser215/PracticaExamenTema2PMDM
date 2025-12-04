package com.example.a1actividadadobligatoria

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a1actividadadobligatoria.databinding.ActivityLlamadaBinding

class Llamada : AppCompatActivity() {
    private lateinit var binding: ActivityLlamadaBinding

    private lateinit var numero: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLlamadaBinding.inflate(layoutInflater)
        setContentView(binding.llamada)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.llamada.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        numero = intent.getStringExtra("numero") ?: ""

        //Boton de llamar
        binding.btnllamar.setOnClickListener {
            if (numero != null && numero != ""){
                call()
            }else{
                config()
            }
        }

        // Boton de volver
        binding.imageButton10.setOnClickListener {
            volver()
        }

    }

    private fun config(){
        val intent = Intent(this, configActivity::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        startActivity(intent)
    }


    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {  //creamos la intención
            //Indicamos la Uri que es la forma de indicarle a Android que es un teléfono.
            data = Uri.parse("tel:"+numero!!)
        }
        startActivity(intent)
    }





    private fun volver(){
        val intent = Intent(this, pantallaprincipal::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        startActivity(intent)
    }






















}