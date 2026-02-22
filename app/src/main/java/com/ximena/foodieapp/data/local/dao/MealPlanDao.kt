package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

// DAO del planificador semanal
@Dao
interface MealPlanDao {

    // Devuelve el plan de una semana concreta, ordenado por día y tipo de comida
    @Query("SELECT * FROM meal_plans WHERE userId = :userId AND weekYear = :weekYear ORDER BY dayOfWeek, mealType")
    fun getMealPlanForWeek(userId: String, weekYear: String): Flow<List<MealPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlanEntity)

    // Borra una entrada del plan por su ID
    @Query("DELETE FROM meal_plans WHERE id = :id AND userId = :userId")
    suspend fun deleteMealPlanById(userId: String, id: Int)
}