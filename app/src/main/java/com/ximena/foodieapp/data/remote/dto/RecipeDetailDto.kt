package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String? = null,
    val readyInMinutes: Int? = null,
    val servings: Int? = null,
    val summary: String? = null,
    val instructions: String? = null,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<IngredientDto> = emptyList()
)

data class IngredientDto(
    val id: Int? = null,
    val name: String? = null,
    val original: String? = null
)
