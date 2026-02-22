package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.DeleteUserRecipeUseCase
import com.ximena.foodieapp.domain.usecase.GetUserRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    getUserRecipesUseCase: GetUserRecipesUseCase,
    private val deleteUserRecipeUseCase: DeleteUserRecipeUseCase
) : ViewModel() {

    val myRecipes: StateFlow<List<Recipe>> = getUserRecipesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch { deleteUserRecipeUseCase(recipe) }
    }
}