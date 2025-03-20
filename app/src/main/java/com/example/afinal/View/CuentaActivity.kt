package com.example.afinal.View

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.afinal.databinding.CuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts


class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: CuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->

        if (uri!=null){
            binding.imageUser.setImageURI(uri)
        }else{
            //no ninguna imagen
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Verificar si el usuario está autenticado
        val user = firebaseAuth.currentUser

        if (user != null) {
            binding.txtEmailUsuario.text = user.email
        } else {
            binding.txtEmailUsuario.text = "Usuario no conectado"
            redirectToLogin()
        }




        // gestiona el boton de login del la pagina principal
        binding.creaTarea.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }



        // Gestiona el botón que redirige a la página principal
        binding.maps.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Gestiona el botón que cierra sesión
        binding.sesion.setOnClickListener {
            cerrarSesion()
        }

        // Gestiona el botón que redirige a la actividad de edición de perfil
        binding.textPerfil.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        // Configura el botón de la cámara
        binding.camara.setOnClickListener {

            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

            )
            // Verifica permisos antes de abrir la cámara
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }
    }

    // Función para abrir la cámara
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No se puede acceder a la cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // Maneja el resultado de la cámara
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageUser.setImageBitmap(imageBitmap)  // Muestra la imagen capturada en el ImageView
        }
    }

    // Función para cerrar sesión
    private fun cerrarSesion() {
        firebaseAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        Toast.makeText(this, "Cerraste sesión correctamente", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()  // Cierra la actividad actual
    }

    // Redirige al login si el usuario no está autenticado
    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Maneja los permisos en tiempo de ejecución
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso de la cámara está concedido, abre la cámara
                openCamera()
            } else {
                // Si el permiso no está concedido, solicítalo
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }
    }
}