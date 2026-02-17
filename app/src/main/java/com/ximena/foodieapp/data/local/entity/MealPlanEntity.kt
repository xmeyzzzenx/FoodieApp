package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "meal_plan_entries",
    primaryKeys = ["weekKey", "dayOfWeek", "mealType"]
)
data class MealPlanEntity(
    val weekKey: String,
    val dayOfWeek: Int,
    val mealType: String,

    val isOnline: Boolean,
    val onlineRecipeId: Int? = null,
    val localRecipeId: Long? = null,

    val title: String,
    val image: String?
)
