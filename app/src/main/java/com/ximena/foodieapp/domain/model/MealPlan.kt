package com.ximena.foodieapp.domain.model

// Modelo de un registro del plan semanal
data class MealPlan(
    val id: Int,
    val dayOfWeek: String, // día
    val mealType: String, // tipo (desayuno/comida/cena)
    val recipeId: Int, // id receta
    val recipeTitle: String // nombre receta
)
