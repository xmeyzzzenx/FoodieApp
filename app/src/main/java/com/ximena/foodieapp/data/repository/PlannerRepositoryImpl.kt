package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.FavoriteDao
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.domain.repository.PlannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val mealPlanDao: MealPlanDao
) : PlannerRepository {

    override fun observeFavorites(): Flow<List<FavoriteRecipeEntity>> = favoriteDao.observeAll()

    override fun searchFavorites(query: String): Flow<List<FavoriteRecipeEntity>> = favoriteDao.search(query)

    override fun observeIsFavorite(recipeId: Int): Flow<Boolean> = favoriteDao.observeIsFavorite(recipeId)

    override suspend fun addFavorite(entity: FavoriteRecipeEntity) {
        favoriteDao.insert(entity)
    }

    override suspend fun removeFavorite(recipeId: Int) {
        favoriteDao.delete(recipeId)
    }

    override fun observeMealPlan(): Flow<List<MealPlanEntity>> = mealPlanDao.observeAll()

    override suspend fun saveMealSlot(entity: MealPlanEntity) {
        mealPlanDao.upsert(entity)
    }

    override suspend fun clearMealSlot(day: String, mealType: String) {
        mealPlanDao.clearSlot(day, mealType)
    }
}
