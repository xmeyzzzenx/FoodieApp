package com.ximena.foodieapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.SaveUserRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// Estado del formulario de crear receta
data class RecipeFormState(
    val id: String = UUID.randomUUID().toString(), // ID único generado al abrir el formulario
    val name: String = "",
    val category: String = "",
    val area: String = "",
    val instructions: String = "",
    val thumbnailUrl: String = "",
    val ingredientsText: String = "",  // Se escribe como texto "harina, azúcar, huevo"
    val measuresText: String = "",     // Igual: "2 cups, 1 tsp, 3"
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val nameError: String? = null,         // Mensaje de error si el campo es inválido
    val instructionsError: String? = null
)

@HiltViewModel
class RecipeFormViewModel @Inject constructor(
    private val saveUserRecipeUseCase: SaveUserRecipeUseCase
) : ViewModel() {

    private val _formState = MutableStateFlow(RecipeFormState())
    val formState: StateFlow<RecipeFormState> = _formState.asStateFlow()

    fun loadRecipeForEdit(recipeId: String) { /* extend if needed */ }

    // Cada función actualiza solo su campo y limpia el error si lo había
    fun onNameChange(v: String) { _formState.value = _formState.value.copy(name = v, nameError = null) }
    fun onCategoryChange(v: String) { _formState.value = _formState.value.copy(category = v) }
    fun onAreaChange(v: String) { _formState.value = _formState.value.copy(area = v) }
    fun onInstructionsChange(v: String) { _formState.value = _formState.value.copy(instructions = v, instructionsError = null) }
    fun onThumbnailUrlChange(v: String) { _formState.value = _formState.value.copy(thumbnailUrl = v) }
    fun onIngredientsChange(v: String) { _formState.value = _formState.value.copy(ingredientsText = v) }
    fun onMeasuresChange(v: String) { _formState.value = _formState.value.copy(measuresText = v) }

    fun saveRecipe() {
        val state = _formState.value
        // Validación: nombre e instrucciones son obligatorios
        val nameError = if (state.name.isBlank()) "El nombre es obligatorio" else null
        val instructionsError = if (state.instructions.isBlank()) "Las instrucciones son obligatorias" else null
        if (nameError != null || instructionsError != null) {
            _formState.value = state.copy(nameError = nameError, instructionsError = instructionsError)
            return
        }
        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true)
            // Convierte el texto "harina, azúcar" en lista ["harina", "azúcar"]
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
            saveUserRecipeUseCase(recipe)
            _formState.value = state.copy(isSaving = false, savedSuccessfully = true)
        }
    }
}