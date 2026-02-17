package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.data.remote.mapper.toDomain
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.domain.repository.RecipesRepository
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi
) : RecipesRepository {

    override suspend fun searchRecipes(query: String?): List<Recipe> {
        val response = api.searchRecipes(query = query)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getRecipeDetail(recipeId: Int): RecipeDetail {
        val dto = api.getRecipeDetail(id = recipeId)
        return dto.toDomain()
    }
}
