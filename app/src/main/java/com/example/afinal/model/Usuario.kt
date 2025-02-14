package com.example.afinal.model

class Usuario  (val email: String, val password: String) {

    fun isValidEmail(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(): Boolean = password.length >= 8
}