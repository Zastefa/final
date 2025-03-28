package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.RecuperarpassBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPasswordActivity : AppCompatActivity() {


    private lateinit var binding: RecuperarpassBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecuperarpassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

            binding.voLogin.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }


}
