package com.ximena.foodieapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetRecipeByIdUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel de la pantalla de detalle
class DetailViewModel(
    private val obtenerRecetaPorId: GetRecipeByIdUseCase,
    private val guardarFavorita: SaveFavoriteUseCase,
    private val recetaId: Int
) : ViewModel() {

    // Estado privado
    private val _receta = MutableStateFlow<Recipe?>(null)

    // Estado público
    val receta: StateFlow<Recipe?> = _receta.asStateFlow()

    init {
        cargarReceta()
    }

    // Buscar la receta por ID
    private fun cargarReceta() {
        viewModelScope.launch {
            obtenerRecetaPorId(recetaId).collect { receta ->
                _receta.value = receta
            }
        }
    }

    // Marcar o desmarcar como favorita
    fun toggleFavorita() {
        viewModelScope.launch {
            try {
                val recetaActual = _receta.value ?: return@launch
                guardarFavorita(recetaActual)

                // Actualizar el estado local
                _receta.value = recetaActual.copy(
                    esFavorita = !recetaActual.esFavorita
                )
            } catch (e: Exception) {
                println("Error al guardar favorita: ${e.message}")
            }
        }
    }
}