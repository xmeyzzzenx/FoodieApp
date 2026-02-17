package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey
    val recipeId: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val createdAt: Long = System.currentTimeMillis()
)
