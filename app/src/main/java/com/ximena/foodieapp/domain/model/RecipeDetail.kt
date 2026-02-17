package com.ximena.foodieapp.domain.model

data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val ingredients: List<RecipeIngredient>,
    val instructions: String
)

data class RecipeIngredient(
    val name: String,
    val original: String
)
