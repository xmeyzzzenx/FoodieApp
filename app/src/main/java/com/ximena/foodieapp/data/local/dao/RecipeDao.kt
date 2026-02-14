package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // Insertar o actualizar receta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(receta: RecipeEntity)

    // Obtener todas las recetas
    @Query("SELECT * FROM recetas")
    fun obtenerTodas(): Flow<List<RecipeEntity>>

    // Obtener solo favoritas
    @Query("SELECT * FROM recetas WHERE esFavorita = 1")
    fun obtenerFavoritas(): Flow<List<RecipeEntity>>

    // Buscar recetas por título
    @Query("SELECT * FROM recetas WHERE titulo LIKE '%' || :busqueda || '%'")
    fun buscarPorTitulo(busqueda: String): Flow<List<RecipeEntity>>

    // Actualizar receta
    @Update
    suspend fun actualizar(receta: RecipeEntity)

    // Eliminar receta
    @Delete
    suspend fun eliminar(receta: RecipeEntity)

    // Obtener receta por ID (sin importar si es favorita)
    @Query("SELECT * FROM recetas WHERE id = :recetaId LIMIT 1")
    fun obtenerPorId(recetaId: Int): Flow<RecipeEntity?>
}