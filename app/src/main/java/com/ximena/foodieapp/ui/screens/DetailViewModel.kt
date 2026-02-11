package com.ximena.foodieapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetRecipeDetailUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de detalle
sealed class DetailUiState {
    object Cargando : DetailUiState()
    data class Exito(val receta: Recipe) : DetailUiState()
    data class Error(val mensaje: String) : DetailUiState()
}

class DetailViewModel(
    private val getRecipeDetail: GetRecipeDetailUseCase,
    private val saveFavorite: SaveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Cargando)
    val uiState: StateFlow<DetailUiState> = _uiState

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Cargando
            try {
                val receta = getRecipeDetail(id, "MI_API_KEY")
                _uiState.value = DetailUiState.Exito(receta)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Error al cargar el detalle")
            }
        }
    }

    fun toggleFavorita(recipe: Recipe) {
        viewModelScope.launch {
            saveFavorite(recipe)
        }
    }
}