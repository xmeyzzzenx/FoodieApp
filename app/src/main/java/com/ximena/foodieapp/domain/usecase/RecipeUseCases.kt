package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Category
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Casos de uso de recetas: cada clase delega en el repositorio

class GetCategoriesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(): Result<List<Category>> = repository.getCategories()
}

class GetMealsByCategoryUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(category: String): Result<List<MealSummary>> =
        repository.getMealsByCategory(category)
}

class GetRandomMealUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(): Result<Recipe> = repository.getRandomMeal()
}

// Primero busca en local, si no está la pide a la API (lógica en el repositorio)
class GetRecipeDetailUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: String): Result<Recipe> = repository.getRecipeDetail(id)
}

class SearchMealsUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(query: String): Result<List<MealSummary>> =
        repository.searchMealsByName(query)
}

// Si ya es favorita la quita, si no lo es la añade (toggle)
class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.toggleFavorite(recipe)
}

// Flow = la lista se actualiza sola cuando cambia algo en Room
class GetFavoriteRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.getFavoriteRecipes()
}

class GetUserRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.getUserRecipes()
}

class SaveUserRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.saveUserRecipe(recipe)
}

class DeleteUserRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.deleteUserRecipe(recipe)
}

class UpdateUserRecipeUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.updateUserRecipe(recipe)
}