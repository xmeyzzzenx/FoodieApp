package com.ximena.foodieapp.ui.screens.mealplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.usecase.mealplan.AddMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.mealplan.DeleteMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.mealplan.GetMealPlansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealPlanViewModel(
    private val getMealPlans: GetMealPlansUseCase,
    private val addMealPlan: AddMealPlanUseCase,
    private val deleteMealPlan: DeleteMealPlanUseCase
) : ViewModel() {

    private val _plans = MutableStateFlow<List<MealPlan>>(emptyList())
    val plans: StateFlow<List<MealPlan>> = _plans.asStateFlow()

    val daysOfWeek = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday",
        "Friday", "Saturday", "Sunday"
    )

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner")

    init {
        observePlans()
    }

    private fun observePlans() {
        viewModelScope.launch {
            getMealPlans().collect { list ->
                _plans.value = list
            }
        }
    }

    fun add(dayOfWeek: String, mealType: String, recipeId: Int, recipeTitle: String) {
        viewModelScope.launch {
            val item = MealPlan(
                id = 0,
                dayOfWeek = dayOfWeek,
                mealType = mealType,
                recipeId = recipeId,
                recipeTitle = recipeTitle
            )
            addMealPlan(item)
        }
    }

    fun delete(item: MealPlan) {
        viewModelScope.launch {
            deleteMealPlan(item)
        }
    }
}
