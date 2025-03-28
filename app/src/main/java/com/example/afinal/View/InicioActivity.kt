package com.example.afinal.View

import android.content.Intent
import android.os.Bundle

import android.widget.ImageButton
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity


import com.example.afinal.R
import com.example.afinal.databinding.InicioBinding
import com.google.firebase.auth.FirebaseAuth

class InicioActivity : AppCompatActivity(){

    private lateinit var binding: InicioBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()


        setupEventButtons()
        setupBottomNavigation()
    }



    private fun setupEventButtons() {
        val eventMap = mapOf(
            R.id.event1 to Pair("Viajes ", TaskActivity::class.java),
            R.id.event2 to Pair("Festival de Anime", ListActivity::class.java),
            R.id.event3 to Pair("Tarde de Té Literario", TaskActivity::class.java),
            R.id.event4 to Pair("Conciertos en Vivo", TaskActivity::class.java),
            R.id.event5 to Pair("Quedada", TaskActivity::class.java),
            R.id.event6 to Pair("Torneos Deportivo", TaskActivity::class.java),
            R.id.event7 to Pair("Senderismos en Montaña", TaskActivity::class.java),
            R.id.event8 to Pair("Esquí en la Nieve", TaskActivity::class.java),
            R.id.event9 to Pair("Día en la Piscina", TaskActivity::class.java),
            R.id.event10 to Pair("Parque de Diversiones", TaskActivity::class.java)
        )

        eventMap.forEach { (id, eventInfo) ->
            findViewById<ImageButton>(id)?.setOnClickListener {
                showEventDetail(eventInfo.first, eventInfo.second)
            }
        }
    }

    private fun showEventDetail(eventTitle: String, activityClass: Class<*>) {
        Toast.makeText(this, "Seleccionado: $eventTitle", Toast.LENGTH_SHORT).show()
        Intent(this, activityClass).apply {
            putExtra("EVENT_TITLE", eventTitle)
            startActivity(this)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rese -> {
                    navigateTo(ListActivity::class.java)
                    true
                }
                R.id.inicio -> {
                    Toast.makeText(this, "Ya estás en tu Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.perfil -> {
                    navigateTo(CuentaActivity::class.java)
                    true
                }
                else -> false
            }
        }
        binding.bottomNav.selectedItemId = R.id.inicio
    }


    private fun <T> navigateTo(activityClass: Class<T>) {
        if (this::class.java != activityClass) {
            startActivity(Intent(this, activityClass))

        }
    }



}