package com.ximena.foodieapp.domain.repository

import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.RecipeDetail

interface RecipesRepository {
    suspend fun searchRecipes(query: String?): List<Recipe>
    suspend fun getRecipeDetail(recipeId: Int): RecipeDetail
}
