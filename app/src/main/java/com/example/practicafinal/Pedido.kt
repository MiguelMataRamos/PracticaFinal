package com.example.practicafinal

data class Pedido (
    var id: String? = null,
    var idusuario: String? = null,
    var idcarta: String? = null,
    var nombrecarta: String? = null,
    var precio: Double? = 0.0,
    var fecha: String? = null,
    var estado: String? = "0"
    )
