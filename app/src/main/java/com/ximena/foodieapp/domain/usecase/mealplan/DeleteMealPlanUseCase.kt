package com.ximena.foodieapp.domain.usecase.mealplan

import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan

// Eliminar comida del plan (Room)
class DeleteMealPlanUseCase(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(mealPlan: MealPlan) {
        repository.delete(mealPlan)
    }
}
