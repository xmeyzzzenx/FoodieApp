package com.ximena.foodieapp.domain.model

// Modelo de una entrada del planificador semanal
data class MealPlan(
    val id: Int = 0,
    val recipeId: String,
    val recipeName: String,
    val recipeThumbnail: String,
    val dayOfWeek: DayOfWeek, // Enum, ej: MONDAY
    val mealType: MealType,   // Enum, ej: LUNCH
    val weekYear: String      // Ej: "2024-W10"
)