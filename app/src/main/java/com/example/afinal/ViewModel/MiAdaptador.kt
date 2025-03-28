package com.example.afinal.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.model.MiModelo
import com.example.afinal.R // Make sure to import your R class

class MiAdaptador(private val listaItems: List<MiModelo>) :
    RecyclerView.Adapter<MiAdaptador.MiViewHolder>() {

    class MiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return MiViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val item = listaItems[position]

        holder.tvTitulo.text = item.tvtitulo // Make sure this matches your model's property name
        holder.tvDescripcion.text = item.tvdescripcion // Make sure this matches your model's property name
        holder.imageView.setImageResource(item.imagen)

        holder.itemView.setOnClickListener {
            // Handle item click
            Toast.makeText(holder.itemView.context, "Clic en ${item.tvtitulo}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = listaItems.size
}