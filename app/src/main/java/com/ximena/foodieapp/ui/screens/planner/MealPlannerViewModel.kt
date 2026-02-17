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
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val plannerRepository: PlannerRepository
) : ViewModel() {

    private val _weekKey = MutableStateFlow(currentWeekKey())
    val weekKey: StateFlow<String> = _weekKey.asStateFlow()

    private val _plan = MutableStateFlow<List<MealPlanEntity>>(emptyList())
    val plan: StateFlow<List<MealPlanEntity>> = _plan.asStateFlow()

    init {
        observeCurrentWeek()
    }

    private fun observeCurrentWeek() {
        viewModelScope.launch {
            weekKey.collectLatest { key ->
                plannerRepository.observeMealPlanWeek(key).collectLatest {
                    _plan.value = it
                }
            }
        }
    }

    fun nextWeek() {
        _weekKey.value = shiftWeek(_weekKey.value, 1)
    }

    fun prevWeek() {
        _weekKey.value = shiftWeek(_weekKey.value, -1)
    }

    fun setSlotOnline(dayOfWeek: Int, mealType: String, recipeId: Int, title: String, image: String?) {
        viewModelScope.launch {
            plannerRepository.saveMealSlot(
                MealPlanEntity(
                    weekKey = _weekKey.value,
                    dayOfWeek = dayOfWeek,
                    mealType = mealType,
                    isOnline = true,
                    onlineRecipeId = recipeId,
                    localRecipeId = null,
                    title = title,
                    image = image
                )
            )
        }
    }

    fun setSlotLocal(dayOfWeek: Int, mealType: String, localId: Long, title: String, image: String?) {
        viewModelScope.launch {
            plannerRepository.saveMealSlot(
                MealPlanEntity(
                    weekKey = _weekKey.value,
                    dayOfWeek = dayOfWeek,
                    mealType = mealType,
                    isOnline = false,
                    onlineRecipeId = null,
                    localRecipeId = localId,
                    title = title,
                    image = image
                )
            )
        }
    }

    fun clearSlot(dayOfWeek: Int, mealType: String) {
        viewModelScope.launch {
            plannerRepository.clearMealSlot(_weekKey.value, dayOfWeek, mealType)
        }
    }

    private fun currentWeekKey(): String {
        val today = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val weekNumber = today.get(weekFields.weekOfWeekBasedYear())
        val year = today.get(weekFields.weekBasedYear())
        return "%04d-W%02d".format(year, weekNumber)
    }

    private fun shiftWeek(weekKey: String, delta: Int): String {
        val parts = weekKey.split("-W")
        if (parts.size != 2) return weekKey

        val year = parts[0].toIntOrNull() ?: return weekKey
        val week = parts[1].toIntOrNull() ?: return weekKey

        val weekFields = WeekFields.ISO
        val date = LocalDate.now()
            .with(weekFields.weekBasedYear(), year.toLong())
            .with(weekFields.weekOfWeekBasedYear(), week.toLong())
            .with(weekFields.dayOfWeek(), 1)

        val shifted = date.plusWeeks(delta.toLong())
        val newWeek = shifted.get(weekFields.weekOfWeekBasedYear())
        val newYear = shifted.get(weekFields.weekBasedYear())

        return "%04d-W%02d".format(newYear, newWeek)
    }
}
