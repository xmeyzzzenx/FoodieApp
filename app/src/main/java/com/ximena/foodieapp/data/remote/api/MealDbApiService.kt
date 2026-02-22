package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.CategoryListResponse
import com.ximena.foodieapp.data.remote.dto.MealDetailResponse
import com.ximena.foodieapp.data.remote.dto.MealListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApiService {
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") name: String): MealListResponse

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealDetailResponse

    @GET("categories.php")
    suspend fun getCategories(): CategoryListResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealDetailResponse
}
