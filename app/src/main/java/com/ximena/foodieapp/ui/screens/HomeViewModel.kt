package com.ximena.foodieapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetRecipesUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla principal
sealed class HomeUiState {
    object Cargando : HomeUiState()
    data class Exito(val recetas: List<Recipe>) : HomeUiState()
    data class Error(val mensaje: String) : HomeUiState()
}

class HomeViewModel(
    private val getRecipes: GetRecipesUseCase,
    private val saveFavorite: SaveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Cargando)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        cargarRecetas()
    }

    fun cargarRecetas() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Cargando
            try {
                val recetas = getRecipes("MI_API_KEY")
                _uiState.value = HomeUiState.Exito(recetas)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al cargar recetas")
            }
        }
    }

    fun toggleFavorita(recipe: Recipe) {
        viewModelScope.launch {
            saveFavorite(recipe)
        }
    }
}