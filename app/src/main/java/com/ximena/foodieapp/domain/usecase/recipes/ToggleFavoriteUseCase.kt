package com.ximena.foodieapp.domain.usecase.recipes

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe

// Marcar o desmarcar favorita
class ToggleFavoriteUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        repository.setFavorite(recipe, !recipe.isFavorite)
    }
}
