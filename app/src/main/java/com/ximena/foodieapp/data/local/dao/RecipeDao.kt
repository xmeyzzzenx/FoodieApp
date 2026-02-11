package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

// Consultas a la tabla "recetas"
@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReceta(receta: RecipeEntity)

    @Query("SELECT * FROM recetas WHERE esFavorita = 1")
    fun obtenerFavoritas(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recetas")
    fun obtenerTodas(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recetas WHERE titulo LIKE '%' || :busqueda || '%'")
    fun buscarPorTitulo(busqueda: String): Flow<List<RecipeEntity>>

    @Delete
    suspend fun eliminarReceta(receta: RecipeEntity)

    @Update
    suspend fun actualizarReceta(receta: RecipeEntity)
}