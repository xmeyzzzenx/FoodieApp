package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // ── READ ──────────────────────────────────────────────────────────────

    // Todas las recetas guardadas en Room (por si la usas como caché)
    @Query("SELECT * FROM recipes ORDER BY title ASC")
    fun getAll(): Flow<List<RecipeEntity>>

    // Receta guardada por ID (en Room)
    @Query("SELECT * FROM recipes WHERE id = :recipeId LIMIT 1")
    fun getById(recipeId: Int): Flow<RecipeEntity?>

    // Solo favoritas (en Room)
    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavorites(): Flow<List<RecipeEntity>>

    // Buscar dentro de favoritas por título (en Room)
    @Query(
        "SELECT * FROM recipes " +
                "WHERE isFavorite = 1 AND title LIKE '%' || :query || '%' " +
                "ORDER BY title ASC"
    )
    fun search(query: String): Flow<List<RecipeEntity>>

    // ── WRITE ─────────────────────────────────────────────────────────────

    // Insertar o reemplazar (lo usamos para guardar y para actualizar favorito)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity)

    // Actualizar (opcional: si usas insert(REPLACE) casi no lo necesitas)
    @Update
    suspend fun update(recipe: RecipeEntity)

    // Eliminar (por ejemplo, quitar de favoritas)
    @Delete
    suspend fun delete(recipe: RecipeEntity)
}
