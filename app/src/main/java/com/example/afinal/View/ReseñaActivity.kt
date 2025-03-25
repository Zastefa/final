package com.example.afinal.View

import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.afinal.databinding.EditarBinding
import com.google.firebase.auth.FirebaseAuth


class ReseñaActivity : AppCompatActivity() {

    private lateinit var binding: EditarBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Añadir mensajes de ejemplo
        addMessage(binding.messagesContainer, "User  1", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.")
        addMessage(binding.messagesContainer, "User  2", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.")
        addMessage(binding.messagesContainer, "User  3", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.")
        addMessage(binding.messagesContainer, "User  4", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.")
        addMessage(binding.messagesContainer, "User  5", "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.")
    }

    private fun addMessage(container: LinearLayout, user: String, message: String) {
        // Crear TextView para el usuario
        val userView = TextView(this).apply {
            text = user
            setTypeface(null, Typeface.BOLD)
            textSize = 16f
            setTextColor(ContextCompat.getColor(this@ReseñaActivity, R.color.black))
        }

        // Crear TextView para el mensaje
        val messageView = TextView(this).apply {
            text = message
            textSize = 14f
            setTextColor(ContextCompat.getColor(this@ReseñaActivity, R.color.darker_gray))
            setPadding(0, 0, 0, 24)
        }

        // Añadir al contenedor
        container.addView(userView)
        container.addView(messageView)
    }
}