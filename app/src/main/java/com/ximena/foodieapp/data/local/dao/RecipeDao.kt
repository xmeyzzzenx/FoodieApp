package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

// DAO de recetas: todas las operaciones con la tabla "recipes"
@Dao
interface RecipeDao {

    // Recetas de la API (no creadas por el usuario), ordenadas por nombre
    // Flow = la UI se actualiza sola cuando cambia la BD
    @Query("SELECT * FROM recipes WHERE userId = :userId AND isUserCreated = 0 ORDER BY name ASC")
    fun getAllRecipes(userId: String): Flow<List<RecipeEntity>>

    // Solo favoritas
    @Query("SELECT * FROM recipes WHERE userId = :userId AND isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(userId: String): Flow<List<RecipeEntity>>

    // Solo las creadas por el usuario, las más nuevas primero
    @Query("SELECT * FROM recipes WHERE userId = :userId AND isUserCreated = 1 ORDER BY createdAt DESC")
    fun getUserRecipes(userId: String): Flow<List<RecipeEntity>>

    // Busca por ID (suspend = se ejecuta en corrutina, no bloquea la app)
    @Query("SELECT * FROM recipes WHERE userId = :userId AND id = :id LIMIT 1")
    suspend fun getRecipeById(userId: String, id: String): RecipeEntity?

    // Busca por nombre o categoría, LIKE permite búsqueda parcial
    @Query("SELECT * FROM recipes WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%')")
    fun searchRecipes(userId: String, query: String): Flow<List<RecipeEntity>>

    // Inserta una receta, si ya existe la sobreescribe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    // Inserta varias a la vez
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    // Solo toca el campo isFavorite, no el resto
    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE userId = :userId AND id = :id")
    suspend fun updateFavoriteStatus(userId: String, id: String, isFavorite: Boolean)
}