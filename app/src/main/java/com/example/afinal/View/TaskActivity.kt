package com.example.afinal.View

import Even
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.afinal.R
import com.example.afinal.databinding.TareasBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView

class TaskActivity : AppCompatActivity(), OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: TareasBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var eventoActual: Even // Cambia a Even

    companion object {
        const val EXTRA_EVENTO = "evento_data"

        fun start(context: AppCompatActivity, evento: Even) {
            val intent = Intent(context, TaskActivity::class.java).apply {
                putExtra(EXTRA_EVENTO, evento)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TareasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupBottomNavigation()
        loadEventData()
        initializeMap()
    }

    private fun setupNavigation() {
        binding.navView.setNavigationItemSelectedListener(this)
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

    private fun loadEventData() {
        eventoActual = intent.getParcelableExtra(EXTRA_EVENTO) ?: run {
            showErrorAndFinish(getString(R.string.no_event_data))
            return
        }

        with(binding) {
            tituloEvento.text = eventoActual.titulo
            nombreEvento.text = eventoActual.nombre
            ubicacionEvento.text = eventoActual.ubicacionTexto
            descripcionEvento.text = eventoActual.descripcion

            // Load image
            eventoActual.imagenUrl?.let { url ->
                Glide.with(this@TaskActivity)
                    .load(url)
                    .placeholder(R.drawable.imaeven)
                    .error(R.drawable.imaeven)
                    .into(imagenEvento)
            } ?: imagenEvento.setImageResource(R.drawable.imaeven)

            // Set click listeners
            imagenEvento.setOnLongClickListener {
                shareEvent()
                true
            }

            ubicacionTitulo.setOnClickListener { openNavigation() }
            botonAccion.setOnClickListener { participateInEvent() }
        }
    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.apply {
                isZoomControlsEnabled = true
                isMapToolbarEnabled = true
            }

            eventoActual.ubicacionLat?.let { lat ->
                eventoActual.ubicacionLng?.let { lng ->
                    val location = LatLng(lat, lng)
                    addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(eventoActual.nombre)
                            .snippet(eventoActual.ubicacionTexto)
                    )
                    moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                    setOnMapClickListener { openInGoogleMaps(lat, lng) }
                }
            }
        }
    }

    private fun shareEvent() {
        val shareText = """
            ${eventoActual.titulo}
            
            ${eventoActual.descripcion}
            
            Ubicación: ${eventoActual.ubicacionTexto}
            
            ${eventoActual.imagenUrl?.let { "Ver más: $it" } ?: ""}
        """.trimIndent()

        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(this, getString(R.string.share_event)))
        }
    }

    private fun openNavigation() {
        eventoActual.ubicacionLat?.let { lat ->
            eventoActual.ubicacionLng?.let { lng ->
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("google.navigation:q=$lat,$lng&mode=d")
                    setPackage("com.google.android.apps.maps")
                    if (resolveActivity(packageManager) != null) {
                        startActivity(this)
                    } else {
                        showError(getString(R.string.maps_not_installed))
                    }
                }
            }
        } ?: showError(getString(R.string.location_unavailable))
    }

    private fun openInGoogleMaps(lat: Double, lng: Double) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:$lat,$lng?q=$lat,$lng(${eventoActual.nombre})")
            setPackage("com.google.android.apps.maps")
            if (resolveActivity(packageManager) != null) {
                startActivity(this)
            } else {
                showError(getString(R.string.maps_not_installed))
            }
        }
    }

    private fun participateInEvent() {
        // TODO: Implement participation logic
        showMessage(getString(R.string.participation_registered))
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showErrorAndFinish(message: String) {
        showError(message)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio -> {
                // Already on home, just close drawer
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.perfil -> navigateTo(CuentaActivity::class.java)
            R.id.rese -> navigateTo(ListActivity::class.java)
        }
        return true
    }

    private fun performLogout() {
        // TODO: Implement logout logic
        showMessage(getString(R.string.logging_out))
        finishAffinity()
        startActivity(Intent(this, LoginActivity::class.java))
    }




}
