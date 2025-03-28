package com.example.afinal.View

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.ActivityResenaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ResenaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityResenaBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Configurar MapView
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        setupRatingBar()
        setupSaveButton()
    }

    private fun setupRatingBar() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            Toast.makeText(this, "Calificación: $rating", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSaveButton() {
        binding.butGuardar.setOnClickListener {
            guardarResena()
        }
    }

    private fun guardarResena() {
        val titulo = binding.titol.text.toString().trim()
        val descripcion = binding.des.text.toString().trim()
        val calificacion = binding.ratingBar.rating
        val userId = firebaseAuth.currentUser?.uid ?: ""

        if (validarCampos(titulo, descripcion)) {
            guardarEnFirestore(titulo, descripcion, calificacion, userId)
        }
    }

    private fun validarCampos(titulo: String, descripcion: String): Boolean {
        return when {
            titulo.isEmpty() -> {
                binding.titol.error = "Ingrese un título"
                false
            }
            descripcion.isEmpty() -> {
                binding.des.error = "Ingrese una descripción"
                false
            }
            selectedLocation == null -> {
                Toast.makeText(this, "Seleccione una ubicación en el mapa", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun guardarEnFirestore(
        titulo: String,
        descripcion: String,
        calificacion: Float,
        userId: String
    ) {
        val reseña = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "calificacion" to calificacion,
            "ubicacion" to "${selectedLocation!!.latitude},${selectedLocation!!.longitude}",
            "userId" to userId,
            "fecha" to Calendar.getInstance().time
        )

        firestore.collection("reseñas")
            .add(reseña)
            .addOnSuccessListener {
                Toast.makeText(this, "Reseña guardada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.isZoomControlsEnabled = true
            setOnMapClickListener { latLng ->
                selectedLocation = latLng
                clear()
                addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Ubicación seleccionada")
                )
                getAddressFromLocation(latLng)
            }
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.4168, -3.7038), 12f))
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        try {
            Geocoder(this, Locale.getDefault()).run {
                getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()?.let { address ->
                    val addressText = listOfNotNull(
                        address.thoroughfare,
                        address.featureName,
                        address.locality
                    ).joinToString(", ")

                    Toast.makeText(this@ResenaActivity, "Ubicación: $addressText", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo obtener la dirección", Toast.LENGTH_SHORT).show()
        }
    }

    // Manejo del ciclo de vida del MapView
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}