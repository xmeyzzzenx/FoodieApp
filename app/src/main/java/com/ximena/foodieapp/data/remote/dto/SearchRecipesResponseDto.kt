package com.ximena.foodieapp.data.remote.dto

data class SearchRecipesResponseDto(
    val results: List<RecipeItemDto> = emptyList()
)

data class RecipeItemDto(
    val id: Int,
    val title: String,
    val image: String? = null,
    val readyInMinutes: Int? = null,
    val servings: Int? = null
)
