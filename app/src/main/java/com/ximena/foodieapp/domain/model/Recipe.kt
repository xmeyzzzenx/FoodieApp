package com.ximena.foodieapp.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val category: String? = null,
    val area: String? = null
)
