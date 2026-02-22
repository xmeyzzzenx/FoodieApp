package com.ximena.foodieapp.domain.model

// Modelo de categoría (ej: "Seafood", "Vegetarian")
data class Category(
    val id: String,
    val name: String,
    val thumbnailUrl: String,
    val description: String
)