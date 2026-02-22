package com.ximena.foodieapp.domain.model

data class ShoppingItem(
    val id: Int = 0,
    val name: String,
    val measure: String,
    val isChecked: Boolean = false,
    val recipeId: String? = null,
    val recipeName: String? = null
)
