package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.RecipeDetailDto
import com.ximena.foodieapp.data.remote.dto.SearchRecipesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String?,
        @Query("number") number: Int = 20,
        @Query("addRecipeInformation") addInfo: Boolean = true
    ): SearchRecipesResponseDto

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetail(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): RecipeDetailDto
}
