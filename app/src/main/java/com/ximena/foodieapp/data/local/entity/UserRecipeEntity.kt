package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_recipes")
data class UserRecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val title: String,
    val imageUrl: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val ingredientsText: String,
    val instructionsText: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
