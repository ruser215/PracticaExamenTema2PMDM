package com.example.a1actividadadobligatoria.Adaptadores

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a1actividadadobligatoria.clasespojo.Accion
import com.example.a1actividadadobligatoria.databinding.ItemDiferentesaccionesBinding

class AdaptadorAcciones(
    private val lista: List<Accion>,
    private val onClick: (Accion) -> Unit
) : RecyclerView.Adapter<AdaptadorAcciones.ActionViewHolder>() {

    // aqui se pone la vista que se usa para cada uno de los items
    inner class ActionViewHolder(val binding: ItemDiferentesaccionesBinding)
        : RecyclerView.ViewHolder(binding.root)

    //este metodo crear la view dekl viewholderHotel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val binding = ItemDiferentesaccionesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActionViewHolder(binding)
    }

    //Este metodo renderiza los datos y propiedades de cada accion
    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val accion = lista[position]

        holder.binding.textNombreActividad.text = accion.nombre
        holder.binding.viewRecicladaAcciones.background = ContextCompat.getDrawable(holder.itemView.context, accion.imagen)
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, accion.vista.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
            holder.itemView.context.startActivity(intent)
        }
    }
    //Este metodo devuelve el numero de items
    override fun getItemCount(): Int = lista.size
}
