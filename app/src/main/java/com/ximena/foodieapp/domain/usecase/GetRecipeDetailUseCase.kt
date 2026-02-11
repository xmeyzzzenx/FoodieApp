package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import javax.inject.Inject

// Obtiene el detalle de una receta por su id
class GetRecipeDetailUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: Int, apiKey: String): Recipe {
        return repository.obtenerDetalleReceta(id, apiKey)
    }
}