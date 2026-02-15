package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // ID local
    val dayOfWeek: String,                              // Día
    val mealType: String,                               // Tipo comida
    val recipeId: Int,                                  // ID receta
    val recipeTitle: String                             // Nombre receta
)