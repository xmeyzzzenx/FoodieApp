package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow

// Consultas a la tabla del plan semanal
@Dao
interface MealPlanDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(plan: MealPlanEntity)

    // READ
    @Query("SELECT * FROM plan_semanal ORDER BY diaSemana ASC")
    fun obtenerTodos(): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM plan_semanal WHERE diaSemana = :dia")
    fun obtenerPorDia(dia: String): Flow<List<MealPlanEntity>>

    // UPDATE
    @Update
    suspend fun actualizar(plan: MealPlanEntity)

    // DELETE
    @Delete
    suspend fun eliminar(plan: MealPlanEntity)

    @Query("DELETE FROM plan_semanal")
    suspend fun limpiarTodos()
}