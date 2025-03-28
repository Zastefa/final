package com.example.afinal.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.afinal.R
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

    // Registro para actividad de cámara
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
        checkUserAuth()
        setupButtons()
        setupBottomNavigation()
    }

    private fun checkUserAuth() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            binding.txtEmailUsuario.text = user.email
        } else {
            binding.txtEmailUsuario.text = getString(R.string.user_not_connected)
            redirectToLogin()
        }
    }

    private fun setupBottomNavigation() {
       // binding.buttonNav.setOnItemSelectedListener { item ->
            //when (item.itemId) {
               // R.id.rese -> navigateTo(ReseñaActivity::class.java)
               // R.id.perfil -> navigateTo(CuentaActivity::class.java)
                //R.id.inicio -> { /* Acción para inicio */ }
                //else -> false
            //}
           // true
       // }
    }

    private fun setupButtons() {
        binding.apply {
            camara.setOnClickListener { checkCameraPermissionAndOpen() }
            galleryButton.setOnClickListener { openGallery() }
            creaTarea.setOnClickListener { navigateTo(TaskActivity::class.java) }
            maps.setOnClickListener { navigateTo(MainActivity::class.java) }
            sesion.setOnClickListener { cerrarSesion() }
            textPerfil.setOnClickListener { navigateTo(EditActivity::class.java) }
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
            .setTitle(getString(R.string.camera_permission_title))
            .setMessage(getString(R.string.camera_permission_message))
            .setPositiveButton(getString(R.string.accept)) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir("Pictures")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun openCamera() {
        try {
            val photoFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )
            currentPhotoPath = photoFile.absolutePath
            takePicture.launch(photoURI)
        } catch (ex: IOException) {
            Toast.makeText(this, getString(R.string.file_creation_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun <T> navigateTo(activityClass: Class<T>) {
        startActivity(Intent(this, activityClass))
    }

    private fun cerrarSesion() {
        firebaseAuth.signOut()
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        redirectToLogin()
    }

    private fun redirectToLogin() {
        navigateTo(LoginActivity::class.java)
        finish()
    }
}