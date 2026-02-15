package com.ximena.foodieapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.recipes.GetFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.SearchFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavorites: GetFavoritesUseCase,
    private val searchFavorites: SearchFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val recipes: List<Recipe>) : UiState()
        data object Empty : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavorites().collect { list ->
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list)
            }
        }
    }

    fun onSearch(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            // volvemos al flow normal de favoritas
            observeFavorites()
            return
        }

        searchJob = viewModelScope.launch {
            _state.value = UiState.Loading
            searchFavorites(query).collect { list ->
                _state.value = if (list.isEmpty()) UiState.Empty else UiState.Success(list)
            }
        }
    }

    fun onToggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            toggleFavorite(recipe)
            // No hace falta tocar state aquí: Room emite el cambio y se refresca solo.
        }
    }
}
