package com.example.afinal.model

data class Evento(
    val id: String = "",
    val titulo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val ubicacionTexto: String = "",
    val ubicacionLat: Double? = null,
    val ubicacionLng: Double? = null,
    val imagenUrl: String? = null,
    val creadorId: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)