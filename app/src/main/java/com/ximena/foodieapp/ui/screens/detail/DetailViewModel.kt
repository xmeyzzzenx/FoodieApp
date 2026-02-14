package com.ximena.foodieapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel de la pantalla de detalle
class DetailViewModel(
    private val guardarFavorita: SaveFavoriteUseCase,
    recetaInicial: Recipe
) : ViewModel() {

    // Estado privado
    private val _receta = MutableStateFlow(recetaInicial)

    // Estado público
    val receta: StateFlow<Recipe> = _receta.asStateFlow()

    // Marcar o desmarcar como favorita
    fun toggleFavorita() {
        viewModelScope.launch {
            try {
                val recetaActual = _receta.value
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