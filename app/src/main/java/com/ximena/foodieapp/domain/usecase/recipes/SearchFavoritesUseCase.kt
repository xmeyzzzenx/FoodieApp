package com.ximena.foodieapp.domain.usecase.recipes

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// Buscar favoritas en Room
class SearchFavoritesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(query: String): Flow<List<Recipe>> {
        return repository.searchFavorites(query)
    }
}
