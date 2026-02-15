package com.ximena.foodieapp.domain.usecase.recipes

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// Favoritas guardadas en Room
class GetFavoritesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.getFavorites()
    }
}
