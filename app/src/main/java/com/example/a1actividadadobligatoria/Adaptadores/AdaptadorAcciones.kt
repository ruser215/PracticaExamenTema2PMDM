package com.example.a1actividadadobligatoria.Adaptadores

import androidx.recyclerview.widget.RecyclerView
import com.example.a1actividadadobligatoria.clasespojo.Accion

class AdaptadorAcciones {
    class ItemAdapter(
        private val lista: List<Accion>,
        private val onClick: (Accion) -> Unit
    ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        inner class ItemViewHolder(val binding: ItemViewBinding)
            : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding = ItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = lista[position]

            holder.binding.txtTitulo.text = item.titulo
            holder.binding.txtDescripcion.text = item.descripcion

            holder.binding.root.setOnClickListener {
                onClick(item)
            }
        }

        override fun getItemCount(): Int = lista.size
    }
}