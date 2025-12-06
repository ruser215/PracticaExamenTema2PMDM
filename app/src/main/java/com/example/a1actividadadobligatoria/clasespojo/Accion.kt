package com.example.a1actividadadobligatoria.clasespojo

import android.app.Activity
import kotlin.reflect.KClass

data class Accion(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val vista: KClass<out Activity>,
    val imagen: Int
)
