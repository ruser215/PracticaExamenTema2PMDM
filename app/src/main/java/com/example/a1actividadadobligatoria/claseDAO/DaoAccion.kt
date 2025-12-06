package com.example.a1actividadadobligatoria.claseDAO

import com.example.a1actividadadobligatoria.Interfaces.InterfazAccion
import com.example.a1actividadadobligatoria.Repositorio.AccionesRepositorio.acciones
import com.example.a1actividadadobligatoria.clasespojo.Accion

class DaoAccion private constructor() : InterfazAccion {
    companion object {
        val myDao: DaoAccion by lazy{
            DaoAccion()
        }
    }

    override fun getTodos(): List<Accion> {
        return acciones
    }
}