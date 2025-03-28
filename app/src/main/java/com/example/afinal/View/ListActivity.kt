package com.example.afinal.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.R
import com.example.afinal.ViewModel.MiAdaptador
import com.example.afinal.databinding.ActivityListBinding

import com.example.afinal.model.MiModelo
import com.google.firebase.auth.FirebaseAuth

class ListActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")


    private lateinit var binding: ActivityListBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        // 1. Configure RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.botAdd.setOnClickListener {
            val intent = Intent(this, ResenaActivity::class.java)
            startActivity(intent)
        }

        // 2. Create data list
        val listaItems = listOf(
            MiModelo("FelipeHappy", "Traje una manta y disfruté de las palomitas mientras veía la película con amigos ", R.drawable.user2),
            MiModelo("BBsani", "Asistí al festival de música el pasado fin de semana y fue simplemente increíble. La energía de la multitud era contagiosa y los artistas hicieron un trabajo excepcional. Me encantó la variedad de géneros musicales y la organización del evento. Además, la comida y las bebidas eran deliciosas", R.drawable.user3),
            MiModelo("SonySo", "La fiesta temática fue un éxito total. La decoración era impresionante y todos estaban vestidos de acuerdo al tema. La música era perfecta y el ambiente era festivo. Me encantó participar en los juegos y actividades que organizaron. Fue una gran oportunidad para conocer gente nueva y hacer amigos.", R.drawable.user4)
            // Add more items as needed...
        )

        // 3. Set up adapter
        val adapter = MiAdaptador(listaItems)
        recyclerView.adapter = adapter

        // Optional: Add decoration
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }
}