package com.ximena.foodieapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.recipes.GetRecipesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRecipes: GetRecipesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val recipeRepository: RecipeRepository,
    private val apiKey: String
) : ViewModel() {

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val recipes: List<Recipe>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes(query: String? = null) {
        viewModelScope.launch {
            _state.value = UiState.Loading

            try {
                val recipes = getRecipes(apiKey, query)

                _state.value = if (recipes.isEmpty()) {
                    UiState.Error("No se encontraron recetas")
                } else {
                    UiState.Success(recipes)
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error al cargar recetas")
            }
        }
    }

    fun onToggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            try {
                toggleFavorite(recipe)

                // Actualizar UI local (opcional, para respuesta inmediata)
                val current = _state.value
                if (current is UiState.Success) {
                    _state.value = UiState.Success(
                        current.recipes.map {
                            if (it.id == recipe.id) it.copy(isFavorite = !it.isFavorite) else it
                        }
                    )
                }
            } catch (_: Exception) {
                // Si falla, no reventamos la app
            }
        }
    }

    fun saveTempForDetail(recipe: Recipe) {
        viewModelScope.launch {
            try {
                recipeRepository.save(recipe)
            } catch (_: Exception) {
                // Si falla, no pasa nada: Detail luego puede tirar de API si lo implementas
            }
        }
    }
}
