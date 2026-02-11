package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

// Consultas a la tabla "plan_comidas"
@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlan(plan: MealPlanEntity)

    @Query("SELECT * FROM plan_comidas ORDER BY diaSemana ASC")
    fun obtenerPlanSemanal(): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM plan_comidas WHERE diaSemana = :dia")
    fun obtenerPorDia(dia: String): Flow<List<MealPlanEntity>>

    @Delete
    suspend fun eliminarPlan(plan: MealPlanEntity)

    @Query("DELETE FROM plan_comidas")
    suspend fun limpiarPlan()
}