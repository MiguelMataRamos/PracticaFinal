package com.example.practicafinal

data class Carta(
    var id: String? = null,
    var nombre: String? = null,
    var categoria: String? = null,
    var precio: String? = null,
    var stock: Int = 0,
    var disponible: Boolean = false,
    var imagen: String? = null
)