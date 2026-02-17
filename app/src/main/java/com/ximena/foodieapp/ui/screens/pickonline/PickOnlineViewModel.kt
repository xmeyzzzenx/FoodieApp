package com.ximena.foodieapp.ui.screens.pickonline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.repository.RecipesRepository
import com.ximena.foodieapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickOnlineViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _state = MutableStateFlow<UiState<List<Recipe>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Recipe>>> = _state.asStateFlow()

    init {
        load(null)
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun search() {
        load(_query.value.trim().ifBlank { null })
    }

    fun load(query: String?) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val recipes = recipesRepository.searchRecipes(query)
                _state.value = UiState.Success(recipes)
            } catch (e: Exception) {
                _state.value = UiState.Error("Error cargando recetas")
            }
        }
    }
}
