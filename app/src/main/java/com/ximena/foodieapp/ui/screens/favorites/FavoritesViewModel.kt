package com.ximena.foodieapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import com.ximena.foodieapp.domain.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val plannerRepository: PlannerRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _favorites = MutableStateFlow<List<FavoriteRecipeEntity>>(emptyList())
    val favorites: StateFlow<List<FavoriteRecipeEntity>> = _favorites.asStateFlow()

    private var observeJob: Job? = null

    init {
        observe()
    }

    fun onQueryChange(value: String) {
        _query.value = value
        observe()
    }

    private fun observe() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            val q = _query.value.trim()
            val flow = if (q.isBlank()) {
                plannerRepository.observeFavorites()
            } else {
                plannerRepository.searchFavorites(q)
            }
            flow.collectLatest { _favorites.value = it }
        }
    }

    fun remove(recipeId: Int) {
        viewModelScope.launch {
            plannerRepository.removeFavorite(recipeId)
        }
    }
}
