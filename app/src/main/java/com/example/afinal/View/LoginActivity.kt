package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.afinal.ViewModel.LoginViewModel
import com.example.afinal.databinding.LoginBinding
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginActivity : AppCompatActivity() {


    private lateinit var binding: LoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Analitycs events
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()


        bundle.putString("message","Integracion de firebase completa")
        analytics.logEvent("InitScreen", bundle)


        //Instanciacion de FireBaseAuth y LoginViewModel
        firebaseAuth = Firebase.auth
        viewModel= ViewModelProvider(this)[LoginViewModel::class.java]

        // gestiona el texto que haga de boton y redirija a la  pantalla de registro.
        binding.textSign.setOnClickListener {
            val intent = Intent(this, UsuarioActivity::class.java)
            startActivity(intent)
        }

        binding.butLogin.setOnClickListener{
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val pass = binding.editTextTextPassword.text.toString().trim()


            //Verificacion  si los campos email y pass (contraseña) no están vacíos.

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                //Utilizacion  lel objeto firebaseAuth (que es una instancia de FirebaseAuth) para iniciar sesión
                // con el correo electrónico y la contraseña que el usuario proporciona.

                //addOnCompleteListener maneja el resultado de la operación de autenticación en caso de un error o algun fallo.
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener{ task->
                    if (task.isSuccessful) {

                        val intent = Intent(this, CuentaActivity::class.java) //conecta con el layout del usuario de cuenta
                        startActivity(intent)
                    } else {
                        //mensaje  que gestiona los errores cuando el registro de validacion de los usuarios no son correcto
                        Log.e("LoginError", " ${task.exception?.message}")
                        Toast.makeText(this,  "Error!!!,  no exixte el usuario ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error!!, los campos estan vacio  ", Toast.LENGTH_SHORT).show()
            }   //mensaje de errores
        }


        //authStatus es un LiveData que está expuesto por el ViewModel.
        // Este LiveData contiene el estado de autenticación (por ejemplo, si el inicio de sesión fue exitoso o no).
            viewModel.authStatus.observe(this) { status ->
            val (isSuccess, message) = status
            if (isSuccess) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
            }



    }



}



  





        


       
