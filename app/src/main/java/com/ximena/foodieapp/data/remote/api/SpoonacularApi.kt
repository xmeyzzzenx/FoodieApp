package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.RecipeListDto
import retrofit2.http.GET
import retrofit2.http.Query

// Endpoints de la API de Spoonacular
interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun obtenerRecetas(
        @Query("apiKey") apiKey: String,
        @Query("query") busqueda: String? = null,
        @Query("number") cantidad: Int = 20,
        @Query("addRecipeInformation") infoCompleta: Boolean = true
    ): RecipeListDto
}