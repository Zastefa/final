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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.afinal.R
import com.example.afinal.databinding.CuentaBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: CuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentPhotoPath: String? = null

    // Registro para seleccionar medios
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imageUser.setImageURI(uri)
            // Lógica para subir la imagen a Firebase
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
            // Lógica para subir la imagen a Firebase
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupNavigation()

        firebaseAuth = FirebaseAuth.getInstance()
        checkUserAuth()
        setupButtons()


    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Configuración del BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rese -> {
                    navigateTo(ListActivity::class.java)
                    true
                }
                R.id.inicio -> {
                    navigateTo(InicioActivity::class.java)
                    true
                }
                R.id.perfil -> {
                    //ya nos encontramos en el perfil
                    true
                }
                else -> false
            }
        }

        // Configuración del NavigationView (Drawer)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.rese -> navigateTo(ListActivity::class.java)
                R.id.inicio -> navigateTo(InicioActivity::class.java)
                R.id.perfil-> {
                    //
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Resaltar el ítem actual en el menú
        binding.bottomNav.selectedItemId = R.id.perfil
    }


    private fun checkUserAuth() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            binding.txtEmailUsuario.text = user.email
            binding.nombre.text = user.displayName ?: "Usuario"
        } else {
            binding.txtEmailUsuario.text = getString(R.string.user_not_connected)
            redirectToLogin()
        }
    }

    private fun setupButtons() {
        binding.apply {
            camara.setOnClickListener { checkCameraPermissionAndOpen() }
            galleryButton.setOnClickListener { openGallery() }
            creaTarea.setOnClickListener { navigateTo(TaskActivity::class.java) }
            sesion.setOnClickListener { cerrarSesion() }
            textPerfil.setOnClickListener { navigateTo(ListActivity::class.java) }

            // Configuración única para botLoc
            botLoc.setOnClickListener {
                val intent = Intent(this@CuentaActivity, MainActivity::class.java).apply {
                    putExtra("open_map", true)
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
            }
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
