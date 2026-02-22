package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,          // ID único, viene de la API
    val userId: String,                  // ID de Auth0, para separar datos por usuario
    val name: String,                    // Nombre de la receta
    val category: String,                // Ej: "Seafood"
    val area: String,                    // País de origen
    val instructions: String,            // Pasos para cocinar
    val thumbnailUrl: String,            // URL de la foto
    val tags: String?,                   // Etiquetas opcionales
    val youtubeUrl: String?,             // Video opcional
    val ingredients: String,             // Ingredientes separados por coma
    val measures: String,                // Cantidades, misma posición que ingredients
    val isFavorite: Boolean = false,     // Si está marcada como favorita
    val isUserCreated: Boolean = false,  // true = creada por el usuario, false = de la API
    val createdAt: Long = System.currentTimeMillis() // Fecha de creación en milisegundos
)