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
import java.io.IOException
import java.net.UnknownHostException
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

    // null = todavía no se ha pedido la receta del día
    private val _randomMealState = MutableStateFlow<UiState<Recipe>?>(null)
    val randomMealState: StateFlow<UiState<Recipe>?> = _randomMealState.asStateFlow()

    init {
        loadCategories()
        loadRandomMeal()
    }

    // Devuelve un mensaje de error legible según el tipo de excepción
    private fun errorMessage(error: Throwable, contexto: String): String = when (error) {
        is UnknownHostException -> "Sin conexión a internet. Comprueba tu red."
        is IOException -> "Error de red. Inténtalo de nuevo."
        else -> "Error al cargar $contexto. Inténtalo de nuevo."
    }

    // Carga las categorías y selecciona la primera automáticamente
    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = UiState.Loading
            getCategoriesUseCase().fold(
                onSuccess = { categories ->
                    _categoriesState.value = UiState.Success(categories)
                    categories.firstOrNull()?.let { selectCategory(it.name) }
                },
                onFailure = {
                    _categoriesState.value = UiState.Error(errorMessage(it, "las categorías"))
                }
            )
        }
    }

    // Cambia la categoría seleccionada y carga sus recetas
    fun selectCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _mealsState.value = UiState.Loading
            getMealsByCategoryUseCase(category).fold(
                onSuccess = { _mealsState.value = UiState.Success(it) },
                onFailure = { _mealsState.value = UiState.Error(errorMessage(it, "las recetas")) }
            )
        }
    }

    // Carga la receta del día en silencio (sin mostrar error si falla)
    private fun loadRandomMeal() {
        viewModelScope.launch {
            getRandomMealUseCase().fold(
                onSuccess = { _randomMealState.value = UiState.Success(it) },
                onFailure = { }
            )
        }
    }

    // Recarga la receta del día mostrando estado de carga
    fun refreshRandom() {
        viewModelScope.launch {
            _randomMealState.value = UiState.Loading
            getRandomMealUseCase().fold(
                onSuccess = { _randomMealState.value = UiState.Success(it) },
                onFailure = { _randomMealState.value = UiState.Error(errorMessage(it, "la receta del día")) }
            )
        }
    }
}