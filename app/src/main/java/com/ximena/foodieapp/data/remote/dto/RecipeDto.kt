package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Objeto que devuelve la API para una receta en la lista
data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String,
    @SerializedName("readyInMinutes") // Nombre exacto que usa la API
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String
)