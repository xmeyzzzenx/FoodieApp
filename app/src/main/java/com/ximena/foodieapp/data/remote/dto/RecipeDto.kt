package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Receta básica que devuelve la API en la lista
data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String,
    @SerializedName("readyInMinutes") // Nombre exacto en la API
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String
)