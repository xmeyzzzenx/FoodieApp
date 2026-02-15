package com.ximena.foodieapp.domain.usecase.mealplan

import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan
import kotlinx.coroutines.flow.Flow

// Obtener plan semanal (Room)
class GetMealPlansUseCase(
    private val repository: MealPlanRepository
) {
    operator fun invoke(): Flow<List<MealPlan>> {
        return repository.getAll()
    }
}
