package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Category
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.domain.usecase.GetCategoriesUseCase
import com.ximena.foodieapp.domain.usecase.GetMealsByCategoryUseCase
import com.ximena.foodieapp.domain.usecase.GetRandomMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMealsByCategoryUseCase: GetMealsByCategoryUseCase,
    private val getRandomMealUseCase: GetRandomMealUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoriesState: StateFlow<UiState<List<Category>>> = _categoriesState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _mealsState = MutableStateFlow<UiState<List<MealSummary>>>(UiState.Loading)
    val mealsState: StateFlow<UiState<List<MealSummary>>> = _mealsState.asStateFlow()

    private val _randomMealState = MutableStateFlow<UiState<Recipe>?>(null)
    val randomMealState: StateFlow<UiState<Recipe>?> = _randomMealState.asStateFlow()

    init {
        loadCategories()
        loadRandomMeal()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            getCategoriesUseCase().fold(
                onSuccess = { categories ->
                    _categoriesState.value = UiState.Success(categories)
                    categories.firstOrNull()?.let { selectCategory(it.name) }
                },
                onFailure = { _categoriesState.value = UiState.Error(it.message ?: "Error") }
            )
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _mealsState.value = UiState.Loading
            getMealsByCategoryUseCase(category).fold(
                onSuccess = { _mealsState.value = UiState.Success(it) },
                onFailure = { _mealsState.value = UiState.Error(it.message ?: "Error") }
            )
        }
    }

    private fun loadRandomMeal() {
        viewModelScope.launch {
            getRandomMealUseCase().fold(
                onSuccess = { _randomMealState.value = UiState.Success(it) },
                onFailure = { }
            )
        }
    }

    fun refreshRandom() {
        viewModelScope.launch {
            _randomMealState.value = UiState.Loading
            getRandomMealUseCase().fold(
                onSuccess = { _randomMealState.value = UiState.Success(it) },
                onFailure = { _randomMealState.value = UiState.Error(it.message ?: "Error") }
            )
        }
    }
}
