package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: String,
    val mealType: String,
    val recipeId: Int,
    val title: String,
    val image: String?,
    val isOnline: Boolean
)
