package com.example.afinal.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.R
import com.example.afinal.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFragment()

        //firebaseAuth se utiliza este metodo para realizar acciones como iniciar sesión,
        // cerrar sesión o verificar el estado de autenticación del usuario.
        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun createFragment(){
        val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MapsDebug", "Mapa cargado correctamente")
        map = googleMap
        createMarker()
    }

    private fun createMarker() {
        val coordinates = LatLng( 41.35124, 2.12582)
        val marker: MarkerOptions = MarkerOptions().position(coordinates).title("CEAC tu centro de estudio!!")
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 4000, null)
    }



}













