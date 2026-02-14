package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe

// Obtener recetas de la API
class GetRecipesUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(apiKey: String, busqueda: String? = null): List<Recipe> {
        return repository.obtenerRecetas(apiKey, busqueda)
    }
}