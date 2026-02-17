package com.ximena.foodieapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.domain.repository.PlannerRepository
import com.ximena.foodieapp.domain.repository.RecipesRepository
import com.ximena.foodieapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val plannerRepository: PlannerRepository
) : ViewModel() {

    private val _detailState = MutableStateFlow<UiState<RecipeDetail>>(UiState.Idle)
    val detailState: StateFlow<UiState<RecipeDetail>> = _detailState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private var favJob: Job? = null

    fun loadOnline(recipeId: Int) {
        loadCommon(recipeId)
        observeFavorite(recipeId)
    }

    private fun loadCommon(recipeId: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val detail = recipesRepository.getRecipeDetail(recipeId)
                _detailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = UiState.Error("Error cargando detalle")
            }
        }
    }

    private fun observeFavorite(recipeId: Int) {
        favJob?.cancel()
        favJob = viewModelScope.launch {
            plannerRepository.observeIsFavorite(recipeId).collectLatest {
                _isFavorite.value = it
            }
        }
    }

    fun toggleFavorite() {
        val current = _detailState.value
        if (current !is UiState.Success) return

        val detail = current.data

        viewModelScope.launch {
            if (_isFavorite.value) {
                plannerRepository.removeFavorite(detail.id)
            } else {
                plannerRepository.addFavorite(
                    FavoriteRecipeEntity(
                        recipeId = detail.id,
                        title = detail.title,
                        image = detail.image,
                        readyInMinutes = detail.readyInMinutes,
                        servings = detail.servings
                    )
                )
            }
        }
    }
}
