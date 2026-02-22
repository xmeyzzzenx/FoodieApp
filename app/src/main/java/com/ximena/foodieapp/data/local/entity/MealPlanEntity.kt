package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID que genera Room solo
    val userId: String,          // Usuario dueño del plan
    val recipeId: String,        // ID de la receta planificada
    val recipeName: String,      // Nombre guardado para no tener que buscarlo
    val recipeThumbnail: String, // Foto guardada igual
    val dayOfWeek: String,       // Ej: "Monday"
    val mealType: String,        // Ej: "Lunch"
    val weekYear: String         // Ej: "2024-W10" para filtrar por semana
)