package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetRecipeDetailUseCase
import com.ximena.foodieapp.domain.usecase.SaveUserRecipeUseCase
import com.ximena.foodieapp.domain.usecase.UpdateUserRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

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
    private val saveUserRecipeUseCase: SaveUserRecipeUseCase,
    private val updateUserRecipeUseCase: UpdateUserRecipeUseCase,  // ← nuevo
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase     // ← nuevo
) : ViewModel() {

    private val _formState = MutableStateFlow(RecipeFormState())
    val formState: StateFlow<RecipeFormState> = _formState.asStateFlow()

    // true cuando venimos de editar, false cuando es nueva receta
    private var isEditing = false

    // Carga los datos de la receta existente en el formulario
    fun loadRecipeForEdit(recipeId: String) {
        isEditing = true
        viewModelScope.launch {
            val recipe = getRecipeDetailUseCase(recipeId).getOrNull() ?: return@launch
            _formState.value = _formState.value.copy(
                id = recipe.id,  // ← clave: usa el ID existente, no el UUID nuevo
                name = recipe.name,
                category = recipe.category,
                area = recipe.area,
                instructions = recipe.instructions,
                thumbnailUrl = recipe.thumbnailUrl ?: "",
                ingredientsText = recipe.ingredients.joinToString(", "),
                measuresText = recipe.measures.joinToString(", ")
            )
        }
    }

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
            val recipe = Recipe(
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
            // Si es edición actualiza, si es nueva inserta
            if (isEditing) {
                updateUserRecipeUseCase(recipe)
            } else {
                saveUserRecipeUseCase(recipe)
            }
            _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
        }
    }
}