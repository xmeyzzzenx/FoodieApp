package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe

// Marcar o desmarcar receta como favorita
class SaveFavoriteUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        if (recipe.esFavorita) {
            repository.eliminarFavorita(recipe)
        } else {
            repository.guardarFavorita(recipe.copy(esFavorita = true))
        }
    }
}