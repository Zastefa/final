package com.example.afinal.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.afinal.model.FirebaseAutentication
import com.example.afinal.model.Usuario
import com.example.afinal.databinding.LoginBinding
import com.google.firebase.firestore.auth.User

class LoginViewModel: ViewModel() {

    private lateinit var binding: LoginBinding
    private val authRepository = FirebaseAutentication()

    val authStatus = MutableLiveData<Pair<Boolean, String?>>()
    val fieldErrors = MutableLiveData<String>()

    fun signUp(email: String, password: String){
        val user = Usuario(email, password)

        if (!user.isValidEmail()) {
            fieldErrors.postValue("El correo no es válido, vuelva a validadrse")
            return
        }
        if (!user.isValidPassword()) {
            fieldErrors.postValue("La contraseña debe constar 8 caracteres como minimo ")
            return
        }

        authRepository.registerUser(email, password) { isSuccess, message ->
            authStatus.postValue(Pair(isSuccess, message))
        }
    }

    fun login(email: String, password: String):Result<Unit>{
        val user = Usuario(email, password)

        if (!user.isValidEmail()) {
            fieldErrors.postValue("El correo no es válido, vuelva a validarse")
        }
        if (!user.isValidPassword()) {
            fieldErrors.postValue("La contraseña debe constar 8 caracteres como minimo ")
        }

        authRepository.loginUser(email, password) { isSuccess, message ->
            authStatus.postValue(Pair(isSuccess, message))
        }
        return TODO("Provide the return value")
    }
}