package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.UsuarioBinding
import com.google.firebase.auth.FirebaseAuth


class UsuarioActivity : AppCompatActivity() {


    private lateinit var binding: UsuarioBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()


        //crea variables para cada uno de los campos que se tiene rellenar
        binding.butRegistro.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val pass = binding.editTextPassword.text.toString()



            if (email.isNotEmpty() && pass.isNotEmpty() ) {


                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, CuentaActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "los campos estan vacios no se ha registrado ningun usuario.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
