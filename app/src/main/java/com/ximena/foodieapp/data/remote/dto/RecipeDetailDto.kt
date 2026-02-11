package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Objeto que devuelve la API para el detalle de una receta
data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val instructions: String,
    @SerializedName("extendedIngredients")
    val ingredients: List<IngredientDto>
)

// Objeto que representa un ingrediente dentro de una receta
data class IngredientDto(
    val id: Int,
    val name: String,
    val amount: Double,
    val unit: String
)