package com.ximena.foodieapp.data.remote.dto

// Lista de recetas que devuelve la API
data class RecipeListDto(
    val results: List<RecipeDto>,
    val totalResults: Int
)