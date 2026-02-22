package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.ShoppingItem
import com.ximena.foodieapp.domain.usecase.AddShoppingItemsUseCase
import com.ximena.foodieapp.domain.usecase.ClearCheckedItemsUseCase
import com.ximena.foodieapp.domain.usecase.DeleteShoppingItemUseCase
import com.ximena.foodieapp.domain.usecase.GetMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.GetShoppingItemsUseCase
import com.ximena.foodieapp.domain.usecase.RemoveMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.SaveUserRecipeUseCase
import com.ximena.foodieapp.domain.usecase.ToggleShoppingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// ─── MealPlan ──────────────────────────────────────────────────────────────────

@HiltViewModel
class MealPlanViewModel @Inject constructor(
    private val getMealPlanUseCase: GetMealPlanUseCase,
    private val removeMealPlanUseCase: RemoveMealPlanUseCase,
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    val currentWeek = mealPlanRepository.getCurrentWeekYear()

    val mealPlans: StateFlow<List<MealPlan>> = getMealPlanUseCase(currentWeek)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeMealPlan(id: Int) {
        viewModelScope.launch { removeMealPlanUseCase(id) }
    }
}

// ─── ShoppingList ──────────────────────────────────────────────────────────────

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    getShoppingItemsUseCase: GetShoppingItemsUseCase,
    private val addShoppingItemsUseCase: AddShoppingItemsUseCase,
    private val toggleShoppingItemUseCase: ToggleShoppingItemUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase,
    private val clearCheckedItemsUseCase: ClearCheckedItemsUseCase
) : ViewModel() {

    val shoppingItems: StateFlow<List<ShoppingItem>> = getShoppingItemsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleItem(id: Int, checked: Boolean) {
        viewModelScope.launch { toggleShoppingItemUseCase(id, checked) }
    }

    fun deleteItem(item: ShoppingItem) {
        viewModelScope.launch { deleteShoppingItemUseCase(item) }
    }

    fun clearChecked() {
        viewModelScope.launch { clearCheckedItemsUseCase() }
    }

    fun addItem(name: String, measure: String) {
        if (name.isBlank()) return
        viewModelScope.launch { addShoppingItemsUseCase(ShoppingItem(name = name, measure = measure)) }
    }
}

// ─── RecipeForm ────────────────────────────────────────────────────────────────

data class RecipeFormState(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val category: String = "",
    val area: String = "",
    val instructions: String = "",
    val thumbnailUrl: String = "",
    val ingredientsText: String = "",
    val measuresText: String = "",
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val nameError: String? = null,
    val instructionsError: String? = null
)

@HiltViewModel
class RecipeFormViewModel @Inject constructor(
    private val saveUserRecipeUseCase: SaveUserRecipeUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(RecipeFormState())
    val formState: StateFlow<RecipeFormState> = _formState.asStateFlow()

    fun loadRecipeForEdit(recipeId: String) { /* extend if needed */ }

    fun onNameChange(v: String) { _formState.value = _formState.value.copy(name = v, nameError = null) }
    fun onCategoryChange(v: String) { _formState.value = _formState.value.copy(category = v) }
    fun onAreaChange(v: String) { _formState.value = _formState.value.copy(area = v) }
    fun onInstructionsChange(v: String) { _formState.value = _formState.value.copy(instructions = v, instructionsError = null) }
    fun onThumbnailUrlChange(v: String) { _formState.value = _formState.value.copy(thumbnailUrl = v) }
    fun onIngredientsChange(v: String) { _formState.value = _formState.value.copy(ingredientsText = v) }
    fun onMeasuresChange(v: String) { _formState.value = _formState.value.copy(measuresText = v) }

    fun saveRecipe() {
        val state = _formState.value
        val nameError = if (state.name.isBlank()) "El nombre es obligatorio" else null
        val instructionsError = if (state.instructions.isBlank()) "Las instrucciones son obligatorias" else null
        if (nameError != null || instructionsError != null) {
            _formState.value = state.copy(nameError = nameError, instructionsError = instructionsError)
            return
        }
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            val ingredients = state.ingredientsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val measures = state.measuresText.split(",").map { it.trim() }
            val recipe = com.ximena.foodieapp.domain.model.Recipe(
                id = state.id,
                name = state.name,
                category = state.category,
                area = state.area,
                instructions = state.instructions,
                thumbnailUrl = state.thumbnailUrl,
                tags = emptyList(),
                youtubeUrl = null,
                ingredients = ingredients,
                measures = measures,
                isUserCreated = true
            )
            saveUserRecipeUseCase(recipe)
            _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
        }
    }
}
