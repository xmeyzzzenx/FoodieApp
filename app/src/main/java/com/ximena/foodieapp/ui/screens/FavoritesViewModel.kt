package com.ximena.foodieapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de favoritas
sealed class FavoritesUiState {
    object Cargando : FavoritesUiState()
    data class Exito(val favoritas: List<Recipe>) : FavoritesUiState()
    object Vacio : FavoritesUiState()
}

class FavoritesViewModel(
    private val getFavorites: GetFavoritesUseCase,
    private val saveFavorite: SaveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Cargando)
    val uiState: StateFlow<FavoritesUiState> = _uiState

    init {
        cargarFavoritas()
    }

    private fun cargarFavoritas() {
        viewModelScope.launch {
            getFavorites().collect { favoritas ->
                _uiState.value = if (favoritas.isEmpty()) {
                    FavoritesUiState.Vacio
                } else {
                    FavoritesUiState.Exito(favoritas)
                }
            }
        }
    }

    fun toggleFavorita(recipe: Recipe) {
        viewModelScope.launch {
            saveFavorite(recipe)
        }
    }
}