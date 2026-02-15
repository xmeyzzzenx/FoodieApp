package com.ximena.foodieapp.data.remote.dto

// Respuesta de búsqueda/lista de recetas
data class RecipeListDto(
    val results: List<RecipeDto>, // Lista de recetas
    val totalResults: Int         // Total resultados
)
