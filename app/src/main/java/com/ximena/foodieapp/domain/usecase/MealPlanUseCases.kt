package com.ximena.foodieapp.domain.usecase

import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealPlanUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    operator fun invoke(weekYear: String): Flow<List<MealPlan>> =
        repository.getMealPlanForWeek(weekYear)
}

class AddMealPlanUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(mealPlan: MealPlan) = repository.addMealPlan(mealPlan)
}

class RemoveMealPlanUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(id: Int) = repository.removeMealPlan(id)
}

class GetShoppingItemsUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    operator fun invoke(): Flow<List<ShoppingItem>> = repository.getAllShoppingItems()
}

class AddShoppingItemsUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(items: List<ShoppingItem>) = repository.addShoppingItems(items)
    suspend operator fun invoke(item: ShoppingItem) = repository.addShoppingItem(item)
}

class ToggleShoppingItemUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(id: Int, checked: Boolean) =
        repository.toggleShoppingItem(id, checked)
}

class DeleteShoppingItemUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke(item: ShoppingItem) = repository.deleteShoppingItem(item)
}

class ClearCheckedItemsUseCase @Inject constructor(
    private val repository: MealPlanRepository
) {
    suspend operator fun invoke() = repository.clearCheckedItems()
}
