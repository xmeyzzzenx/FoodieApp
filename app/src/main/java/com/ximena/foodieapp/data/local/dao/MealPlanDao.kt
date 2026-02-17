package com.ximena.foodieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    @Query("SELECT * FROM meal_plan ORDER BY dayOfWeek ASC, mealType ASC")
    fun observeAll(): Flow<List<MealPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MealPlanEntity)

    @Query("DELETE FROM meal_plan WHERE dayOfWeek = :day AND mealType = :mealType")
    suspend fun clearSlot(day: String, mealType: String)
}
