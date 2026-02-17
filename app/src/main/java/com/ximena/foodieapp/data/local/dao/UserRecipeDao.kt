package com.ximena.foodieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: UserRecipeEntity): Long

    @Update
    suspend fun update(recipe: UserRecipeEntity)

    @Query("DELETE FROM user_recipes WHERE localId = :localId")
    suspend fun delete(localId: Long)

    @Query("SELECT * FROM user_recipes ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<UserRecipeEntity>>

    @Query("SELECT * FROM user_recipes WHERE title LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun search(query: String): Flow<List<UserRecipeEntity>>

    @Query("SELECT * FROM user_recipes WHERE localId = :localId LIMIT 1")
    suspend fun getById(localId: Long): UserRecipeEntity?
}
