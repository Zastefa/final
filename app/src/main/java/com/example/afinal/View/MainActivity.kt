package com.example.afinal.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.afinal.R
import com.example.afinal.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    companion object {
        private const val REQUEST_CODE_LOCATION = 100
        private const val DEFAULT_ZOOM_LEVEL = 18f
        private val CEAC_COORDINATES = LatLng(41.35124, 2.12582)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebaseAuth()
        setupUI()
        initMapFragment()


        // Si viene desde el botón de ubicación, ignora la selección del BottomNav
        if (intent?.hasExtra("open_map") == true) {
            initMapFragment()  // Inicializa el mapa directamente
        } else {
            setupBottomNavigation()  // Configuración normal
        }
    }

    private fun initFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun setupUI() {
        setupActionBar()
        setupNavigationDrawer()
        setupBottomNavigation()
        setupFabButton()
    }

    private fun setupActionBar() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        ).apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.event -> navigateTo(TaskActivity::class.java)
                R.id.ñ_event -> navigateTo(TaskActivity::class.java)
                R.id.h_event -> navigateTo(ListActivity::class.java)
                R.id.ubi -> { /* Already here */ }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rese -> navigateTo(ListActivity::class.java)
                R.id.inicio -> navigateTo(InicioActivity::class.java)
                R.id.perfil -> navigateTo(CuentaActivity::class.java)
                else -> false
            }
            true
        }
        binding.bottomNav.selectedItemId = R.id.inicio
    }

    private fun setupFabButton() {
        binding.fabMenu.setOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun <T : AppCompatActivity> navigateTo(activityClass: Class<T>) {
        startActivity(Intent(this, activityClass))
    }

    private fun initMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this) ?: showError("Map fragment not found")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setOnMyLocationButtonClickListener(this@MainActivity)
            setOnMyLocationClickListener(this@MainActivity)
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
        createMarker()
        enableLocation()
    }

    private fun createMarker() {
        try {
            map.addMarker(
                MarkerOptions()
                    .position(CEAC_COORDINATES)
                    .title("CEAC tu centro de estudio!!")
            )
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(CEAC_COORDINATES, DEFAULT_ZOOM_LEVEL),
                4000,
                null
            )
        } catch (e: Exception) {
            showError("Error creating map marker: ${e.message}")
        }
    }

    private fun enableLocation() {
        if (!::map.isInitialized) return

        when {
            isLocationPermissionGranted() -> {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                }
            }
            shouldShowPermissionRationale() -> {
                showToast("Please enable location permissions in settings for better experience")
            }
            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowPermissionRationale() = ActivityCompat.shouldShowRequestPermissionRationale(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation()
                } else {
                    showToast("Location permission denied. Some features will be limited")
                }
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()) {
            map.isMyLocationEnabled = false
            showToast("To enable location, please go to settings and accept permissions")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        toggleDrawer()
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        showToast("Centering on your current location")
        return false
    }

    override fun onMyLocationClick(location: Location) {
        showToast("You're at ${location.latitude}, ${location.longitude}")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Log.e("MainActivity", message)
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }
}