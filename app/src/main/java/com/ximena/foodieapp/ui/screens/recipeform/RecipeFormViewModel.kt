package com.ximena.foodieapp.ui.screens.recipeform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import com.ximena.foodieapp.domain.repository.UserRecipesRepository
import com.ximena.foodieapp.utils.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeFormState(
    val title: String = "",
    val imageUrl: String = "",
    val ingredientsText: String = "",
    val instructionsText: String = "",
    val error: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class RecipeFormViewModel @Inject constructor(
    private val userRecipesRepository: UserRecipesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeFormState())
    val state: StateFlow<RecipeFormState> = _state.asStateFlow()

    private var currentLocalId: Long = 0L
    private var createdAt: Long = 0L

    fun load(localId: Long) {
        if (localId <= 0L) return
        currentLocalId = localId

        viewModelScope.launch {
            val entity = userRecipesRepository.getById(localId) ?: return@launch
            createdAt = entity.createdAt

            _state.value = _state.value.copy(
                title = entity.title,
                imageUrl = entity.imageUrl.orEmpty(),
                ingredientsText = entity.ingredientsText,
                instructionsText = entity.instructionsText,
                error = null
            )
        }
    }

    fun onTitleChange(v: String) = update { copy(title = v) }
    fun onImageUrlChange(v: String) = update { copy(imageUrl = v) }
    fun onIngredientsChange(v: String) = update { copy(ingredientsText = v) }
    fun onInstructionsChange(v: String) = update { copy(instructionsText = v) }

    private fun update(block: RecipeFormState.() -> RecipeFormState) {
        _state.value = _state.value.block().copy(error = null)
    }

    fun save(onDone: () -> Unit) {
        val s = _state.value

        if (!Validators.isNotBlank(s.title)) {
            _state.value = s.copy(error = "El título es obligatorio")
            return
        }
        if (!Validators.isNotBlank(s.ingredientsText)) {
            _state.value = s.copy(error = "Ingredientes obligatorios")
            return
        }
        if (!Validators.isNotBlank(s.instructionsText)) {
            _state.value = s.copy(error = "Instrucciones obligatorias")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)

            val now = System.currentTimeMillis()
            val entity = UserRecipeEntity(
                localId = currentLocalId,
                title = s.title.trim(),
                imageUrl = s.imageUrl.trim().ifBlank { null },
                ingredientsText = s.ingredientsText.trim(),
                instructionsText = s.instructionsText.trim(),
                createdAt = if (currentLocalId > 0L) createdAt else now,
                updatedAt = now
            )

            if (currentLocalId <= 0L) {
                val newId = userRecipesRepository.insert(entity)
                currentLocalId = newId
                createdAt = entity.createdAt
            } else {
                userRecipesRepository.update(entity)
            }

            _state.value = _state.value.copy(isSaving = false)
            onDone()
        }
    }
}
