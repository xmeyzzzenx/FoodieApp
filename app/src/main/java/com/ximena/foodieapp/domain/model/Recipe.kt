package com.ximena.foodieapp.domain.model

data class Recipe(
    val id: Int,                        // Identificador único de la receta
    val titulo: String,                 // Nombre de la receta
    val imagen: String,                 // URL de la foto
    val minutosPreparacion: Int,        // Tiempo de preparación en minutos
    val porciones: Int,                 // Número de porciones
    val descripcion: String,            // Descripción breve
    val esFavorita: Boolean = false     // Si está guardada como favorita (por defecto no)
)