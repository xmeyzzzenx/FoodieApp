package com.ximena.foodieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    // Observa SOLO la semana seleccionada
    @Query("""
        SELECT * FROM meal_plan_entries
        WHERE weekKey = :weekKey
        ORDER BY dayOfWeek ASC, mealType ASC
    """)
    fun observeWeek(weekKey: String): Flow<List<MealPlanEntity>>

    // REPLACE reemplaza el slot correctamente
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MealPlanEntity)

    @Query("""
        DELETE FROM meal_plan_entries
        WHERE weekKey = :weekKey AND dayOfWeek = :day AND mealType = :mealType
    """)
    suspend fun clearSlot(weekKey: String, day: Int, mealType: String)

    // Opcional pero útil: limpiar toda la semana de golpe
    @Query("DELETE FROM meal_plan_entries WHERE weekKey = :weekKey")
    suspend fun clearWeek(weekKey: String)
}
