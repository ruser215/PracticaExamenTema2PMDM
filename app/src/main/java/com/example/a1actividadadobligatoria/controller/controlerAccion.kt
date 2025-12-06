package com.example.a1actividadadobligatoria.controller

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.a1actividadadobligatoria.Adaptadores.AdaptadorAcciones
import com.example.a1actividadadobligatoria.claseDAO.DaoAccion
import com.example.a1actividadadobligatoria.clasespojo.Accion
import com.example.a1actividadadobligatoria.pantallaprincipal

class controlerAccion (val context: Context) {
    lateinit var listAcciones : MutableList<Accion>



    init {
        initData()
    }

    fun initData(){
        listAcciones = DaoAccion.myDao.getTodos().toMutableList()
    }

    fun loggOut() {
        Toast.makeText( context, "He mostrado los datos en pantalla", Toast. LENGTH_LONG).show()
        listAcciones.forEach {
            print(it)
        }
    }
    fun setAdapter(recyclerView: RecyclerView) { // Cargamos nuestro Adapter al RecyclerView
        val myActivity = context as pantallaprincipal
        recyclerView.adapter = AdaptadorAcciones(listAcciones) {
            // El click se gestiona en el propio adaptador.
        }
    }



}