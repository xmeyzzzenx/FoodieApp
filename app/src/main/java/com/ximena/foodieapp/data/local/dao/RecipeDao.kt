package com.ximena.foodieapp.data.local.dao

import androidx.room.*
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE userId = :userId AND isUserCreated = 0 ORDER BY name ASC")
    fun getAllRecipes(userId: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE userId = :userId AND isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(userId: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE userId = :userId AND isUserCreated = 1 ORDER BY createdAt DESC")
    fun getUserRecipes(userId: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE userId = :userId AND id = :id LIMIT 1")
    suspend fun getRecipeById(userId: String, id: String): RecipeEntity?

    @Query("SELECT * FROM recipes WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%')")
    fun searchRecipes(userId: String, query: String): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE userId = :userId AND id = :id")
    suspend fun updateFavoriteStatus(userId: String, id: String, isFavorite: Boolean)
}