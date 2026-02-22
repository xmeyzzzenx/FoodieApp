package com.ximena.foodieapp.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnailUrl: String,
    val tags: List<String>,
    val youtubeUrl: String?,
    val ingredients: List<String>,
    val measures: List<String>,
    val isFavorite: Boolean = false,
    val isUserCreated: Boolean = false
) {
    fun getIngredientsWithMeasures(): List<Pair<String, String>> =
        ingredients.mapIndexed { index, ingredient ->
            ingredient to (measures.getOrNull(index) ?: "")
        }
}
