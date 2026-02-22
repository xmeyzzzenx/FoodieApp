package com.ximena.foodieapp.domain.model

// Modelo de item de la lista de la compra
data class ShoppingItem(
    val id: Int = 0,
    val name: String,
    val measure: String,
    val isChecked: Boolean = false,
    val recipeId: String? = null,   // null si se añadió a mano
    val recipeName: String? = null
)