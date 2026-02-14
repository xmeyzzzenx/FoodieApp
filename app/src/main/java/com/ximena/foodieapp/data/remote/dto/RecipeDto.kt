package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Receta básica de la API
data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String
)