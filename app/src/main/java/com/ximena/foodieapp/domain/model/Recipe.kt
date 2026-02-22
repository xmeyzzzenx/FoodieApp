package com.ximena.foodieapp.domain.model

// Modelo completo de receta usado en toda la app (dominio)
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
    // Combina ingredientes y cantidades en pares [(harina, 2 cups), (huevo, 1)]
    // Se usa en la pantalla de detalle para mostrarlos juntos
    fun getIngredientsWithMeasures(): List<Pair<String, String>> =
        ingredients.mapIndexed { index, ingredient ->
            ingredient to (measures.getOrNull(index) ?: "")
        }
}