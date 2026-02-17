package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.UserRecipeDao
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import com.ximena.foodieapp.domain.repository.UserRecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRecipesRepositoryImpl @Inject constructor(
    private val dao: UserRecipeDao
) : UserRecipesRepository {

    override fun observeAll(): Flow<List<UserRecipeEntity>> = dao.observeAll()

    override fun search(query: String): Flow<List<UserRecipeEntity>> = dao.search(query)

    override suspend fun getById(localId: Long): UserRecipeEntity? = dao.getById(localId)

    override suspend fun insert(recipe: UserRecipeEntity): Long = dao.insert(recipe)

    override suspend fun update(recipe: UserRecipeEntity) = dao.update(recipe)

    override suspend fun delete(localId: Long) = dao.delete(localId)
}
