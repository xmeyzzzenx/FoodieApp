package com.ximena.foodieapp.ui.screens.explore

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.repository.AuthRepository
import com.ximena.foodieapp.domain.repository.RecipesRepository
import com.ximena.foodieapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _recipesState = MutableStateFlow<UiState<List<Recipe>>>(UiState.Idle)
    val recipesState: StateFlow<UiState<List<Recipe>>> = _recipesState.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.userName.collectLatest { _userName.value = it }
        }
        viewModelScope.launch {
            authRepository.userEmail.collectLatest { _userEmail.value = it }
        }
        loadRecipes(null)
    }

    fun onQueryChange(newValue: String) {
        _query.value = newValue
    }

    fun search() {
        loadRecipes(_query.value.trim().ifBlank { null })
    }

    fun loadRecipes(query: String?) {
        viewModelScope.launch {
            _recipesState.value = UiState.Loading
            try {
                val recipes = recipesRepository.searchRecipes(query)
                _recipesState.value = UiState.Success(recipes)
            } catch (e: Exception) {
                _recipesState.value = UiState.Error("Error cargando recetas")
            }
        }
    }

    fun logout(activity: Activity, onDone: () -> Unit) {
        authRepository.logout(activity)
        onDone()
    }
}
