package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

// Consultas a la tabla de recetas
@Dao
interface RecipeDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(receta: RecipeEntity)

    // READ
    @Query("SELECT * FROM recetas")
    fun obtenerTodas(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recetas WHERE esFavorita = 1")
    fun obtenerFavoritas(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recetas WHERE titulo LIKE '%' || :busqueda || '%'")
    fun buscarPorTitulo(busqueda: String): Flow<List<RecipeEntity>>

    // UPDATE
    @Update
    suspend fun actualizar(receta: RecipeEntity)

    // DELETE
    @Delete
    suspend fun eliminar(receta: RecipeEntity)
}