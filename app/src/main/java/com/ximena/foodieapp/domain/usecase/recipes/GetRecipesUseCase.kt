package com.ximena.foodieapp.domain.usecase.recipes

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe

// Recetas desde API (lista/búsqueda)
class GetRecipesUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(apiKey: String, query: String? = null): List<Recipe> {
        return repository.fetchRecipes(apiKey, query)
    }
}
