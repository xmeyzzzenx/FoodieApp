package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.usecase.GetMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.RemoveMealPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val getMealPlanUseCase: GetMealPlanUseCase,
    private val removeMealPlanUseCase: RemoveMealPlanUseCase,
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    val currentWeek = mealPlanRepository.getCurrentWeekYear()

    val mealPlans: StateFlow<List<MealPlan>> = getMealPlanUseCase(currentWeek)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeMealPlan(id: Int) {
        viewModelScope.launch { removeMealPlanUseCase(id) }
    }
}