package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// Obtener receta por ID
class GetRecipeByIdUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(recetaId: Int): Flow<Recipe?> {
        return repository.obtenerPorId(recetaId)
    }
}