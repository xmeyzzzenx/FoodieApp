package com.ximena.foodieapp.domain.model

// Modelo interno de receta que usa la app
data class Recipe(
    val id: Int,
    val titulo: String,
    val imagen: String,
    val minutosPreparacion: Int,
    val porciones: Int,
    val descripcion: String,
    val esFavorita: Boolean = false
)