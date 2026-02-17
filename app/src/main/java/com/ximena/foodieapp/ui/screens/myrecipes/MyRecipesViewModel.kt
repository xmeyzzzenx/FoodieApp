package com.ximena.foodieapp.ui.screens.myrecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import com.ximena.foodieapp.domain.repository.UserRecipesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val userRecipesRepository: UserRecipesRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _recipes = MutableStateFlow<List<UserRecipeEntity>>(emptyList())
    val recipes: StateFlow<List<UserRecipeEntity>> = _recipes.asStateFlow()

    init {
        observe()
    }

    fun onQueryChange(value: String) {
        _query.value = value
        observe()
    }

    private fun observe() {
        viewModelScope.launch {
            val q = _query.value.trim()
            val flow = if (q.isBlank()) userRecipesRepository.observeAll() else userRecipesRepository.search(q)
            flow.collectLatest { _recipes.value = it }
        }
    }

    fun delete(localId: Long) {
        viewModelScope.launch {
            userRecipesRepository.delete(localId)
        }
    }
}
