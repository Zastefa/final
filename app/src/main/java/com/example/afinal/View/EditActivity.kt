package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.afinal.ViewModel.LoginViewModel
import com.example.afinal.databinding.EditarBinding
import com.example.afinal.databinding.LoginBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth



class EditActivity : AppCompatActivity() {


    private lateinit var binding: EditarBinding
    private lateinit var firebaseAuth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = EditarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // gestiona el texto que haga de boton y redirija a la  pantalla de registro.
        binding.save.setOnClickListener {
            val intent = Intent(this, CuentaActivity::class.java)
            startActivity(intent)
        }
    }
}