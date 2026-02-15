package com.ximena.foodieapp.domain.usecase.mealplan

import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan

// Añadir comida al plan (Room)
class AddMealPlanUseCase(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(mealPlan: MealPlan) {
        repository.insert(mealPlan)
    }
}
