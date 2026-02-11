package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Detalle completo de una receta con ingredientes e instrucciones
data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val instructions: String,
    @SerializedName("extendedIngredients") // Nombre exacto en la API
    val ingredients: List<IngredientDto>
)

// Ingrediente dentro de una receta
data class IngredientDto(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String
)