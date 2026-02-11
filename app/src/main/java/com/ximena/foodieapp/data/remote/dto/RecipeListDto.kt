package com.ximena.foodieapp.data.remote.dto

// La API devuelve las recetas dentro de un objeto "results"
// Este DTO representa ese objeto contenedor
data class RecipeListDto(
    val results: List<RecipeDto>,
    val totalResults: Int
)