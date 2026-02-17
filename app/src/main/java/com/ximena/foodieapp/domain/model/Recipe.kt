package com.ximena.foodieapp.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?
)
