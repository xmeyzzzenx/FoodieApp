package com.ximena.foodieapp.domain.repository

import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import kotlinx.coroutines.flow.Flow

interface UserRecipesRepository {
    fun observeAll(): Flow<List<UserRecipeEntity>>
    fun search(query: String): Flow<List<UserRecipeEntity>>
    suspend fun getById(localId: Long): UserRecipeEntity?
    suspend fun insert(recipe: UserRecipeEntity): Long
    suspend fun update(recipe: UserRecipeEntity)
    suspend fun delete(localId: Long)
}
