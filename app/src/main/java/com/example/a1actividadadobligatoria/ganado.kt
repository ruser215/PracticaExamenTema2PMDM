package com.example.a1actividadadobligatoria

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a1actividadadobligatoria.databinding.ActivityChistesBinding
import com.example.a1actividadadobligatoria.databinding.ActivityConfigBinding
import com.example.a1actividadadobligatoria.databinding.ActivityGanadoBinding
import com.google.android.libraries.geo.mapcore.internal.model.b

class ganado : AppCompatActivity() {

    private lateinit var binding : ActivityGanadoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGanadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init(){
        binding.btnVolver.setOnClickListener {
            volver()
        }
    }

    private fun volver(){
        val intent = Intent(this, pantallaprincipal::class.java)
            .apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        startActivity(intent)
    }

}