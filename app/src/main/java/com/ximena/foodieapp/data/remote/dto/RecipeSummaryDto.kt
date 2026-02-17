package com.ximena.foodieapp.data.remote.dto

data class RecipeSummaryDto(
    val id: Int = 0,
    val title: String? = null,
    val image: String? = null,
    val readyInMinutes: Int? = null,
    val servings: Int? = null
)
