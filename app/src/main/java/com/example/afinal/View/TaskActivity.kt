package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.ActivityMainBinding
import com.example.afinal.databinding.TareasBinding
import com.google.firebase.auth.FirebaseAuth

class TaskActivity : AppCompatActivity() {


    private lateinit var binding: TareasBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = TareasBinding.inflate(layoutInflater)
        setContentView(binding.root)






    }

}