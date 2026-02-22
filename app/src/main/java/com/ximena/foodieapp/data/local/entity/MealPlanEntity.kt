package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: String,
    val recipeName: String,
    val recipeThumbnail: String,
    val dayOfWeek: String,
    val mealType: String,
    val weekYear: String
)
