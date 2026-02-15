package com.ximena.foodieapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.recipes.GetRecipeByIdUseCase
import com.ximena.foodieapp.domain.usecase.recipes.ToggleFavoriteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getRecipeById: GetRecipeByIdUseCase,
    private val recipeRepository: RecipeRepository,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val apiKey: String,
    private val recipeId: Int
) : ViewModel() {

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val recipe: Recipe) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var job: Job? = null

    init {
        load()
    }

    fun reload() {
        load()
    }

    private fun load() {
        job?.cancel()
        _state.value = UiState.Loading

        job = viewModelScope.launch {
            // 1) Intentar Room primero
            getRecipeById(recipeId).collect { local ->
                if (local != null) {
                    _state.value = UiState.Success(local)
                } else {
                    // 2) Si no existe en Room -> API
                    fetchFromApiAndCache()
                }
            }
        }
    }

    private fun fetchFromApiAndCache() {
        viewModelScope.launch {
            try {
                val remote = recipeRepository.fetchRecipeById(apiKey = apiKey, recipeId = recipeId)
                recipeRepository.save(remote) // cache en Room
                _state.value = UiState.Success(remote)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error al cargar el detalle")
            }
        }
    }

    fun onToggleFavorite() {
        viewModelScope.launch {
            val current = (_state.value as? UiState.Success)?.recipe ?: return@launch
            toggleFavorite(current)

            // update rápido en UI
            _state.value = UiState.Success(current.copy(isFavorite = !current.isFavorite))
        }
    }
}
