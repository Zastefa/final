package com.example.afinal.View

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.afinal.databinding.CuentaBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: CuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentPhotoPath: String? = null

    // Códigos de solicitud
    private companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val CAMERA_REQUEST_CODE = 101
    }

    // Registro para seleccionar medios
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imageUser.setImageURI(uri)
            // Aquí puedes agregar la lógica para subir la imagen a Firebase
            // uploadImageToFirebase(uri)
        }
    }

    // Registro para permisos de cámara
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // Registro para actividad de cámara (alternativa moderna a startActivityForResult)
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoPath != null) {
            val uri = Uri.fromFile(File(currentPhotoPath))
            binding.imageUser.setImageURI(uri)
            // Aquí puedes agregar la lógica para subir la imagen a Firebase
            // uploadImageToFirebase(uri)
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

        // Configurar listeners de los botones
        setupButtons()
    }

    private fun setupButtons() {
        // Botón para abrir la cámara
        binding.camara.setOnClickListener {
            checkCameraPermissionAndOpen()
        }

        // Botón para seleccionar de la galería
        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        // Botón para crear tarea
        binding.creaTarea.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }

        // Botón para ir a maps
        binding.maps.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Botón para cerrar sesión
        binding.sesion.setOnClickListener {
            cerrarSesion()
        }

        // Botón para editar perfil
        binding.textPerfil.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                showPermissionExplanationDialog()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permiso de cámara necesario")
            .setMessage("Esta aplicación necesita acceso a la cámara para tomar fotos de perfil")
            .setPositiveButton("Aceptar") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Crear archivo para la foto
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show()
            null
        }

        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                it
            )
            currentPhotoPath = it.absolutePath
            takePicture.launch(photoURI)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Crear un nombre de archivo único
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir("Pictures")

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefijo */
            ".jpg", /* sufijo */
            storageDir /* directorio */
        ).apply {
            // Guardar ruta del archivo para uso posterior
            currentPhotoPath = absolutePath
        }
    }

    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun cerrarSesion() {
        firebaseAuth.signOut()
        Toast.makeText(this, "Cerraste sesión correctamente", Toast.LENGTH_SHORT).show()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // Manejo de resultados antiguo (para compatibilidad)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                binding.imageUser.setImageBitmap(it)
            }
        }
    }
}