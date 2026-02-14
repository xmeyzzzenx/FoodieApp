package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// Buscar en favoritas por título
class SearchFavoritesUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(busqueda: String): Flow<List<Recipe>> {
        return repository.buscarEnFavoritas(busqueda)
    }
}