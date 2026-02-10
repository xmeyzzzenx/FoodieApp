package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {

    // Guarda un nuevo registro en el plan semanal
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlan(plan: MealPlanEntity)

    // Devuelve todos los registros del plan semanal ordenados por día
    @Query("SELECT * FROM plan_comidas ORDER BY diaSemana ASC")
    fun obtenerPlanSemanal(): Flow<List<MealPlanEntity>>

    // Devuelve los registros de un día concreto
    @Query("SELECT * FROM plan_comidas WHERE diaSemana = :dia")
    fun obtenerPorDia(dia: String): Flow<List<MealPlanEntity>>

    // Elimina un registro del plan
    @Delete
    suspend fun eliminarPlan(plan: MealPlanEntity)

    // Elimina todos los registros del plan semanal
    @Query("DELETE FROM plan_comidas")
    suspend fun limpiarPlan()
}