package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetFavoriteRecipesUseCase
import com.ximena.foodieapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    // Lista de favoritas, se actualiza sola cuando cambia Room
    // WhileSubscribed(5000) = para el Flow 5s después de que no haya observers (ahorra recursos)
    val favoriteRecipes: StateFlow<List<Recipe>> = getFavoriteRecipesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Llama al toggle, si ya era favorita la quita
    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch { toggleFavoriteUseCase(recipe) }
    }
}