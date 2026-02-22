package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Category
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) = repository.toggleFavorite(recipe)
}

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
