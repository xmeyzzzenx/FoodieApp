package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.RecipeDto
import com.ximena.foodieapp.data.remote.dto.RecipeListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoints de Spoonacular (Retrofit)
interface SpoonacularApi {

    // Buscar recetas (lista principal)
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String, // API key
        @Query("query") query: String? = null, // texto búsqueda
        @Query("number") number: Int = 20, // cantidad
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true // info extra
    ): RecipeListDto

    // Obtener detalle de receta por id
    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int, // id receta
        @Query("apiKey") apiKey: String // API key
    ): RecipeDto
}