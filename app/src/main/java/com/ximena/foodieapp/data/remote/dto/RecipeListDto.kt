package com.ximena.foodieapp.data.remote.dto

// Contenedor que devuelve la API con la lista de recetas
data class RecipeListDto(
    val results: List<RecipeDto>, // Lista de recetas
    val totalResults: Int         // Total de recetas disponibles
)