package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.CategoryListResponse
import com.ximena.foodieapp.data.remote.dto.MealDetailResponse
import com.ximena.foodieapp.data.remote.dto.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz de Retrofit: define los endpoints de TheMealDB
// Retrofit genera el código de red automáticamente a partir de estas anotaciones
interface MealDbApiService {

    // GET themealdb.com/search.php?s=chicken → busca recetas por nombre
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") name: String): MealListResponse

    // GET .../lookup.php?i=52772 → detalle de una receta por ID
    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealDetailResponse

    // GET .../categories.php → lista todas las categorías
    @GET("categories.php")
    suspend fun getCategories(): CategoryListResponse

    // GET .../filter.php?c=Seafood → recetas de una categoría concreta
    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    // GET .../random.php → receta aleatoria
    @GET("random.php")
    suspend fun getRandomMeal(): MealDetailResponse
}