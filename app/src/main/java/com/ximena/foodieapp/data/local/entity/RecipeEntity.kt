package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,                 // ID de Spoonacular
    val title: String,                       // Título
    val imageUrl: String?,                   // URL imagen
    val readyInMinutes: Int,                 // Minutos
    val servings: Int,                       // Porciones
    val summary: String,                     // Descripción
    val isFavorite: Boolean = false          // Favorita
)