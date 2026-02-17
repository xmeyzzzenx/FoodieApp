package com.ximena.foodieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TheMealDbLookupResponseDto(
    @SerializedName("meals") val meals: List<TheMealDbMealDto>?
)
