package com.example.afinal.View

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.UsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class UsuarioActivity : AppCompatActivity() {

    private lateinit var binding: UsuarioBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Configurar el click listener para el campo de fecha
        binding.editTextFecha.setOnClickListener {
            showDatePicker()
        }

        binding.butRegistro.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString()
            val email = binding.editTextEmail.text.toString()
            val pass = binding.editTextPassword.text.toString()
            val fecha = binding.editTextFecha.text.toString()

            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Formatear la fecha como dd/MM/yyyy
            val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
            binding.editTextFecha.setText(formattedDate)
        }, year, month, day)

        // Configurar fecha máxima (hoy)
        datePicker.datePicker.maxDate = calendar.timeInMillis
        datePicker.show()
    }
}