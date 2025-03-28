package com.example.afinal.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.afinal.R
import com.example.afinal.model.Evento

class EventoAdaptar(
    private val eventos: List<Evento>,
    private val onItemClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdaptar.EventoViewHolder>() {

    inner class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo: TextView = itemView.findViewById(R.id.tvTitulo)
        private val descripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        private val imagen: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(evento: Evento) {
            titulo.text = evento.titulo
            descripcion.text = evento.descripcion

            // Load image with Glide
            if (!evento.imagenUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(evento.imagenUrl)
                    .placeholder(R.drawable.user2) // Single placeholder image
                    .error(R.drawable.user3)      // Error image if loading fails
                    .into(imagen)
            } else {
                // Default image if no URL is provided
                imagen.setImageResource(R.drawable.user4)
            }

            itemView.setOnClickListener { onItemClick(evento) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    override fun getItemCount() = eventos.size
}