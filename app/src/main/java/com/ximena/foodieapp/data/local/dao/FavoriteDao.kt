package com.ximena.foodieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_recipes ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT * FROM favorite_recipes WHERE title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun search(query: String): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipeId = :recipeId)")
    fun observeIsFavorite(recipeId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE recipeId = :recipeId")
    suspend fun delete(recipeId: Int)
}
