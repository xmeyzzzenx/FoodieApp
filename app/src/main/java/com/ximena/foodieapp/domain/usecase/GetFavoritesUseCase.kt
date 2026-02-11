package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Obtiene las recetas favoritas guardadas localmente
class GetFavoritesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.obtenerFavoritas()
    }
}