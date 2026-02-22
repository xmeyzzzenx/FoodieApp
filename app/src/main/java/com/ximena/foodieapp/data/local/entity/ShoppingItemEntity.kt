package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val name: String,               // Nombre del ingrediente
    val measure: String,            // Cantidad, ej: "2 cups"
    val isChecked: Boolean = false, // Si ya está metido en el carrito
    val recipeId: String?,          // De qué receta viene (null si se añadió a mano)
    val recipeName: String?         // Nombre de esa receta
)