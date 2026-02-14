package com.ximena.foodieapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import com.ximena.foodieapp.domain.usecase.SearchFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel de la pantalla de favoritas
class FavoritesViewModel(
    private val obtenerFavoritas: GetFavoritesUseCase,
    private val guardarFavorita: SaveFavoriteUseCase,
    private val buscarFavoritas: SearchFavoritesUseCase
) : ViewModel() {

    // Estado privado
    private val _estado = MutableStateFlow<EstadoUi>(EstadoUi.Cargando)

    // Estado público
    val estado: StateFlow<EstadoUi> = _estado.asStateFlow()

    // Estados posibles de la pantalla
    sealed class EstadoUi {
        object Cargando : EstadoUi()
        data class Exito(val recetas: List<Recipe>) : EstadoUi()
        object Vacio : EstadoUi()
    }

    // Cargar favoritas al iniciar
    init {
        cargarFavoritas()
    }

    // Cargar favoritas desde Room
    fun cargarFavoritas() {
        viewModelScope.launch {
            obtenerFavoritas().collect { recetas ->
                _estado.value = if (recetas.isEmpty()) {
                    EstadoUi.Vacio
                } else {
                    EstadoUi.Exito(recetas)
                }
            }
        }
    }

    // Buscar en favoritas
    fun buscar(texto: String) {
        if (texto.isBlank()) {
            cargarFavoritas()
            return
        }

        viewModelScope.launch {
            buscarFavoritas(texto).collect { recetas ->
                _estado.value = if (recetas.isEmpty()) {
                    EstadoUi.Vacio
                } else {
                    EstadoUi.Exito(recetas)
                }
            }
        }
    }

    // Eliminar de favoritas
    fun eliminarFavorita(receta: Recipe) {
        viewModelScope.launch {
            try {
                guardarFavorita(receta)
                // El Flow se actualiza automáticamente
            } catch (e: Exception) {
                println("Error al eliminar favorita: ${e.message}")
            }
        }
    }
}