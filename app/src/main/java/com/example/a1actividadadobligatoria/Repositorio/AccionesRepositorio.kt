package com.example.a1actividadadobligatoria.Repositorio

import com.example.a1actividadadobligatoria.Llamada
import com.example.a1actividadadobligatoria.R
import com.example.a1actividadadobligatoria.clasespojo.Accion

object AccionesRepositorio {
    val acciones = listOf(
        Accion(1,"Llamada","Llama de emergencia al contacto anteriormente configurado", Llamada::class,R.drawable.img_llamadas_view)

    )
}