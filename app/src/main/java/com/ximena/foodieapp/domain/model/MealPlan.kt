package com.ximena.foodieapp.domain.model

data class MealPlan(
    val id: Int = 0,
    val recipeId: String,
    val recipeName: String,
    val recipeThumbnail: String,
    val dayOfWeek: DayOfWeek,
    val mealType: MealType,
    val weekYear: String
)
