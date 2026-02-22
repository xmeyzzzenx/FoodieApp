package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.domain.usecase.SearchMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMealsUseCase: SearchMealsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<MealSummary>>?>(null)
    val searchResults: StateFlow<UiState<List<MealSummary>>?> = _searchResults.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.isBlank()) { _searchResults.value = null; return }
        searchJob = viewModelScope.launch {
            delay(500)
            _searchResults.value = UiState.Loading
            searchMealsUseCase(query).fold(
                onSuccess = { _searchResults.value = UiState.Success(it) },
                onFailure = { error ->
                    val mensaje = when {
                        error is java.net.UnknownHostException -> "Sin conexión a internet. Comprueba tu red."
                        error is java.io.IOException -> "Error de red. Inténtalo de nuevo."
                        else -> "Error al buscar recetas. Inténtalo de nuevo."
                    }
                    _searchResults.value = UiState.Error(mensaje)
                }
            )
        }
    }
}
