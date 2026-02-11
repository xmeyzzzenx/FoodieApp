package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.RecipeDetailDto
import com.ximena.foodieapp.data.remote.dto.RecipeListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Endpoints de la API de Spoonacular
interface SpoonacularApi {

    // Obtiene lista de recetas
    // URL: /recipes/complexSearch?apiKey=XXX&number=20
    @GET("recipes/complexSearch")
    suspend fun obtenerRecetas(
        @Query("apiKey") apiKey: String,
        @Query("number") cantidad: Int = 20,
        @Query("addRecipeInformation") infoCompleta: Boolean = true
    ): RecipeListDto

    // Obtiene detalle de una receta por id
    // URL: /recipes/123/information?apiKey=XXX
    @GET("recipes/{id}/information")
    suspend fun obtenerDetalleReceta(
        @Path("id") id: Int, // Reemplaza {id} en la URL
        @Query("apiKey") apiKey: String
    ): RecipeDetailDto

    // Busca recetas por nombre
    // URL: /recipes/complexSearch?query=pasta&apiKey=XXX
    @GET("recipes/complexSearch")
    suspend fun buscarRecetas(
        @Query("query") busqueda: String,
        @Query("apiKey") apiKey: String,
        @Query("number") cantidad: Int = 20
    ): RecipeListDto
}