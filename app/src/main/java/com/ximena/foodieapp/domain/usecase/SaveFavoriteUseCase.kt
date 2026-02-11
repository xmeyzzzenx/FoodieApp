package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import javax.inject.Inject

// Guarda o elimina una receta de favoritas
class SaveFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        // Si ya es favorita la elimina, si no la guarda
        if (recipe.esFavorita) {
            repository.eliminarFavorita(recipe)
        } else {
            repository.guardarFavorita(recipe)
        }
    }
}