package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

// @Dao le dice a Room que esta interfaz contiene consultas a la base de datos
@Dao
interface RecipeDao {

    // Guarda una receta. Si ya existe (mismo id) la reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarReceta(receta: RecipeEntity)

    // Devuelve todas las recetas guardadas como favoritas
    @Query("SELECT * FROM recetas WHERE esFavorita = 1")
    fun obtenerFavoritas(): Flow<List<RecipeEntity>>

    // Devuelve todas las recetas guardadas
    @Query("SELECT * FROM recetas")
    fun obtenerTodas(): Flow<List<RecipeEntity>>

    // Busca recetas por título
    @Query("SELECT * FROM recetas WHERE titulo LIKE '%' || :busqueda || '%'")
    fun buscarPorTitulo(busqueda: String): Flow<List<RecipeEntity>>

    // Elimina una receta
    @Delete
    suspend fun eliminarReceta(receta: RecipeEntity)

    // Actualiza una receta (por ejemplo para marcarla como favorita)
    @Update
    suspend fun actualizarReceta(receta: RecipeEntity)
}