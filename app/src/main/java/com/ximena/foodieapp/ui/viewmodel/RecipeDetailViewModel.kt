package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.DayOfWeek
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.MealType
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.ShoppingItem
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.domain.usecase.AddMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.AddShoppingItemsUseCase
import com.ximena.foodieapp.domain.usecase.GetRecipeDetailUseCase
import com.ximena.foodieapp.domain.usecase.ToggleFavoriteUseCase
import com.ximena.foodieapp.data.repository.MealPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addMealPlanUseCase: AddMealPlanUseCase,
    private val addShoppingItemsUseCase: AddShoppingItemsUseCase,
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _recipeState = MutableStateFlow<UiState<Recipe>>(UiState.Loading)
    val recipeState: StateFlow<UiState<Recipe>> = _recipeState.asStateFlow()

    // Mensaje puntual para mostrar en Snackbar (se limpia después de mostrarlo)
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Carga la receta: primero busca en local, si no la pide a la API
    fun loadRecipe(id: String) {
        viewModelScope.launch {
            _recipeState.value = UiState.Loading
            getRecipeDetailUseCase(id).fold(
                onSuccess = { _recipeState.value = UiState.Success(it) },
                onFailure = { error ->
                    val mensaje = when {
                        error is java.net.UnknownHostException -> "Sin conexión a internet. Comprueba tu red."
                        error is java.io.IOException -> "Error de red. Inténtalo de nuevo."
                        else -> "Error al cargar la receta. Inténtalo de nuevo."
                    }
                    _recipeState.value = UiState.Error(mensaje)
                }
            )
        }
    }

    // Cambia el estado de favorito y actualiza la UI sin relanzar la petición
    fun toggleFavorite() {
        val current = (_recipeState.value as? UiState.Success)?.data ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(current)
            _recipeState.value = UiState.Success(current.copy(isFavorite = !current.isFavorite))
            _snackbarMessage.value = if (!current.isFavorite) "Añadida a favoritos" else "Eliminada de favoritos"
        }
    }

    // Convierte los ingredientes de la receta en items de la lista de la compra
    fun addToShoppingList(recipe: Recipe) {
        viewModelScope.launch {
            val items = recipe.getIngredientsWithMeasures().map { (ingredient, measure) ->
                ShoppingItem(name = ingredient, measure = measure, recipeId = recipe.id, recipeName = recipe.name)
            }
            addShoppingItemsUseCase(items)
            _snackbarMessage.value = "Ingredientes añadidos a la lista de compras"
        }
    }

    // Añade la receta al planificador en el día y tipo de comida indicados
    fun addToMealPlan(recipe: Recipe, day: DayOfWeek, mealType: MealType) {
        viewModelScope.launch {
            addMealPlanUseCase(
                MealPlan(
                    recipeId = recipe.id,
                    recipeName = recipe.name,
                    recipeThumbnail = recipe.thumbnailUrl,
                    dayOfWeek = day,
                    mealType = mealType,
                    weekYear = mealPlanRepository.getCurrentWeekYear()
                )
            )
            _snackbarMessage.value = "Añadido al plan de comidas"
        }
    }

    // La pantalla llama a esto después de mostrar el Snackbar para no repetirlo
    fun clearSnackbar() { _snackbarMessage.value = null }
}