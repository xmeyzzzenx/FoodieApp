package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.domain.model.MealPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Repositorio de plan semanal (Room)
class MealPlanRepository(
    private val dao: MealPlanDao
) {

    fun getAll(): Flow<List<MealPlan>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    fun getByDay(day: String): Flow<List<MealPlan>> =
        dao.getByDay(day).map { list -> list.map { it.toDomain() } }

    suspend fun insert(item: MealPlan) {
        dao.insert(item.toEntity())
    }

    suspend fun delete(item: MealPlan) {
        dao.delete(item.toEntity())
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    // ── MAPPERS ───────────────────────────────────────────────────────────

    private fun MealPlanEntity.toDomain(): MealPlan = MealPlan(
        id = id,
        dayOfWeek = dayOfWeek,
        mealType = mealType,
        recipeId = recipeId,
        recipeTitle = recipeTitle
    )

    private fun MealPlan.toEntity(): MealPlanEntity = MealPlanEntity(
        id = id,
        dayOfWeek = dayOfWeek,
        mealType = mealType,
        recipeId = recipeId,
        recipeTitle = recipeTitle
    )
}
