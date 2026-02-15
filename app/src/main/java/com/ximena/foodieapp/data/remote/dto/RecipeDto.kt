package com.ximena.foodieapp.data.remote.dto

// DTO de receta (Spoonacular)
data class RecipeDto(
    val id: Int,                // ID receta
    val title: String,          // Título
    val image: String?,         // URL imagen
    val readyInMinutes: Int,    // Minutos preparación
    val servings: Int,          // Porciones
    val summary: String         // Descripción
)