package com.example.a1actividadadobligatoria.Repositorio

import com.example.a1actividadadobligatoria.Llamada
import com.example.a1actividadadobligatoria.R
import com.example.a1actividadadobligatoria.chistes
import com.example.a1actividadadobligatoria.clasespojo.Accion
import com.example.a1actividadadobligatoria.pantalladados

object AccionesRepositorio {
    val acciones = listOf(
        Accion(1,"Llamada","Llama de emergencia al contacto anteriormente configurado", Llamada::class,R.drawable.img_llamadas_view),
        Accion(2,"Dados","Funcion de dados para hacer tiradas", pantalladados::class, R.drawable.img_dados_view),
        Accion(3,"Chistes", "Un bot te lee un chiste", chistes::class, R.drawable.img_chistes_view)
    )
}