package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TheMealDbSearchResponseDto(
    @SerializedName("meals") val meals: List<TheMealDbMealDto>?
)
