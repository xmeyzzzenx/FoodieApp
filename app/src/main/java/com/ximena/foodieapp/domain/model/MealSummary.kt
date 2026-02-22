package com.ximena.foodieapp.domain.model

// Versión resumida de una receta (solo lo necesario para mostrar en listas)
data class MealSummary(
    val id: String,
    val name: String,
    val thumbnailUrl: String
)