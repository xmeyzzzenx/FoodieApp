package com.ximena.foodieapp.domain.usecase.recipes

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// Receta guardada en Room por id
class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: Int): Flow<Recipe?> {
        return repository.getById(recipeId)
    }
}
