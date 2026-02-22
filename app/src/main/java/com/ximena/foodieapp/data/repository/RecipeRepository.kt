package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.remote.api.MealDbApiService
import com.ximena.foodieapp.data.remote.mapper.toCategory
import com.ximena.foodieapp.data.remote.mapper.toEntity
import com.ximena.foodieapp.data.remote.mapper.toMealSummary
import com.ximena.foodieapp.data.remote.mapper.toRecipe
import com.ximena.foodieapp.data.remote.mapper.toRecipeEntity
import com.ximena.foodieapp.domain.model.Category
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val apiService: MealDbApiService,
    private val recipeDao: RecipeDao,
    private val authRepository: AuthRepository
) {
    private val userId get() = authRepository.getCurrentUserId()

    fun getFavoriteRecipes(): Flow<List<Recipe>> =
        recipeDao.getFavoriteRecipes(userId).map { it.map { e -> e.toRecipe() } }

    fun getUserRecipes(): Flow<List<Recipe>> =
        recipeDao.getUserRecipes(userId).map { it.map { e -> e.toRecipe() } }

    fun searchLocalRecipes(query: String): Flow<List<Recipe>> =
        recipeDao.searchRecipes(userId, query).map { it.map { e -> e.toRecipe() } }

    suspend fun searchMealsByName(name: String): Result<List<MealSummary>> = runCatching {
        apiService.searchMealsByName(name).meals?.map { it.toMealSummary() } ?: emptyList()
    }

    suspend fun getMealsByCategory(category: String): Result<List<MealSummary>> = runCatching {
        apiService.getMealsByCategory(category).meals?.map { it.toMealSummary() } ?: emptyList()
    }

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        apiService.getCategories().categories?.map { it.toCategory() } ?: emptyList()
    }

    suspend fun getRecipeDetail(id: String): Result<Recipe> = runCatching {
        val local = recipeDao.getRecipeById(userId, id)
        if (local != null) return Result.success(local.toRecipe())
        val meal = apiService.getMealById(id).meals?.firstOrNull()
            ?: error("Receta no encontrada")
        recipeDao.insertRecipe(meal.toRecipeEntity(userId = userId))
        meal.toRecipe()
    }

    suspend fun getRandomMeal(): Result<Recipe> = runCatching {
        val meal = apiService.getRandomMeal().meals?.firstOrNull()
            ?: error("No se pudo obtener receta aleatoria")
        meal.toRecipe()
    }

    suspend fun toggleFavorite(recipe: Recipe) {
        val existing = recipeDao.getRecipeById(userId, recipe.id)
        if (existing != null) {
            recipeDao.updateFavoriteStatus(userId, recipe.id, !existing.isFavorite)
        } else {
            recipeDao.insertRecipe(recipe.copy(isFavorite = true).toEntity(userId))
        }
    }

    suspend fun saveUserRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe.copy(isUserCreated = true).toEntity(userId))
    }

    suspend fun deleteUserRecipe(recipe: Recipe) {
        recipeDao.getRecipeById(userId, recipe.id)?.let { recipeDao.deleteRecipe(it) }
    }

    suspend fun updateUserRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.toEntity(userId))
    }
}