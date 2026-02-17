package com.ximena.foodieapp.domain.repository

import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {

    fun observeFavorites(): Flow<List<FavoriteRecipeEntity>>
    fun searchFavorites(query: String): Flow<List<FavoriteRecipeEntity>>
    fun observeIsFavorite(recipeId: Int): Flow<Boolean>

    suspend fun addFavorite(entity: FavoriteRecipeEntity)
    suspend fun removeFavorite(recipeId: Int)

    fun observeMealPlan(): Flow<List<MealPlanEntity>>
    suspend fun saveMealSlot(entity: MealPlanEntity)
    suspend fun clearMealSlot(day: String, mealType: String)
}
