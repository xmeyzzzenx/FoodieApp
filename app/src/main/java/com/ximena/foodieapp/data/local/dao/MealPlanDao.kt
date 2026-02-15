package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    // Obtener todo el plan
    @Query("SELECT * FROM meal_plans ORDER BY dayOfWeek ASC")
    fun getAll(): Flow<List<MealPlanEntity>>

    // Obtener por día
    @Query("SELECT * FROM meal_plans WHERE dayOfWeek = :day ORDER BY mealType ASC")
    fun getByDay(day: String): Flow<List<MealPlanEntity>>

    // Insertar o reemplazar
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MealPlanEntity)

    // Actualizar
    @Update
    suspend fun update(item: MealPlanEntity)

    // Eliminar
    @Delete
    suspend fun delete(item: MealPlanEntity)

    // Borrar todo
    @Query("DELETE FROM meal_plans")
    suspend fun deleteAll()
}
