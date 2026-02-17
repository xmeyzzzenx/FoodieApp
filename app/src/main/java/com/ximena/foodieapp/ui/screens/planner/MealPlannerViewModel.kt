package com.ximena.foodieapp.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.domain.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val plannerRepository: PlannerRepository
) : ViewModel() {

    private val _plan = MutableStateFlow<List<MealPlanEntity>>(emptyList())
    val plan: StateFlow<List<MealPlanEntity>> = _plan.asStateFlow()

    init {
        viewModelScope.launch {
            plannerRepository.observeMealPlan().collectLatest {
                _plan.value = it
            }
        }
    }

    fun setSlotOnline(day: String, mealType: String, recipeId: Int, title: String, image: String?) {
        viewModelScope.launch {
            plannerRepository.saveMealSlot(
                MealPlanEntity(
                    dayOfWeek = day,
                    mealType = mealType,
                    recipeId = recipeId,
                    title = title,
                    image = image,
                    isOnline = true
                )
            )
        }
    }

    fun setSlotLocal(day: String, mealType: String, localId: Long, title: String, image: String?) {
        viewModelScope.launch {
            plannerRepository.saveMealSlot(
                MealPlanEntity(
                    dayOfWeek = day,
                    mealType = mealType,
                    recipeId = localId.toInt(),
                    title = title,
                    image = image,
                    isOnline = false
                )
            )
        }
    }

    fun clearSlot(day: String, mealType: String) {
        viewModelScope.launch {
            plannerRepository.clearMealSlot(day, mealType)
        }
    }
}
