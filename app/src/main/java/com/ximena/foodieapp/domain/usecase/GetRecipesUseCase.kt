package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import javax.inject.Inject

// Obtiene la lista de recetas de la API
class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(apiKey: String): List<Recipe> {
        return repository.obtenerRecetas(apiKey)
    }
}