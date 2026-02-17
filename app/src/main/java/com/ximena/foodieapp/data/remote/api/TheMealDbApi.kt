package com.ximena.foodieapp.data.remote.api

import com.ximena.foodieapp.data.remote.dto.TheMealDbLookupResponseDto
import com.ximena.foodieapp.data.remote.dto.TheMealDbSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMealDbApi {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String?
    ): TheMealDbSearchResponseDto

    @GET("lookup.php")
    suspend fun getMealDetail(
        @Query("i") id: String
    ): TheMealDbLookupResponseDto

    @GET("random.php")
    suspend fun getRandomMeal(): TheMealDbLookupResponseDto
}
