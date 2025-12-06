package com.example.a1actividadadobligatoria.viewholder

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a1actividadadobligatoria.clasespojo.Accion
import com.example.a1actividadadobligatoria.configActivity
import com.example.a1actividadadobligatoria.databinding.ItemDiferentesaccionesBinding

class viewAcciones {
    /*
Recibe la vista creada por el adaptador y mapeo de los datos en la vista.
*/
    class ViewHHotel (view: View) : RecyclerView.ViewHolder (view){
        lateinit var binding: ItemDiferentesaccionesBinding
        init {
            binding = ItemDiferentesaccionesBinding.bind(view)
        }
        //método que se encarga de mapear los item por propiedad del modelo.
        fun renderize(accion: Accion){
            binding.textNombreActividad.setText(accion.nombre)
            binding.viewRecicladaAcciones.setBackgroundResource(accion.imagen)
            binding.viewRecicladaAcciones.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, accion.vista.java)
                context.startActivity(intent)
            }
        }
    }
}