package com.ximena.foodieapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.usecase.GetRecipesUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel de la pantalla principal
class HomeViewModel(
    private val obtenerRecetas: GetRecipesUseCase,
    private val guardarFavorita: SaveFavoriteUseCase,
    private val repository: RecipeRepository,
    private val apiKey: String
) : ViewModel() {

    // Estado privado (solo el ViewModel puede modificarlo)
    private val _estado = MutableStateFlow<EstadoUi>(EstadoUi.Cargando)

    // Estado público (la pantalla solo puede leerlo)
    val estado: StateFlow<EstadoUi> = _estado.asStateFlow()

    // Estados posibles de la pantalla
    sealed class EstadoUi {
        object Cargando : EstadoUi()
        data class Exito(val recetas: List<Recipe>) : EstadoUi()
        data class Error(val mensaje: String) : EstadoUi()
    }

    // Cargar recetas al iniciar
    init {
        cargarRecetas()
    }

    // Cargar recetas de la API
    fun cargarRecetas(busqueda: String? = null) {
        viewModelScope.launch {
            _estado.value = EstadoUi.Cargando

            try {
                val recetas = obtenerRecetas(apiKey, busqueda)

                if (recetas.isEmpty()) {
                    _estado.value = EstadoUi.Error("No se encontraron recetas")
                } else {
                    _estado.value = EstadoUi.Exito(recetas)
                }
            } catch (e: Exception) {
                _estado.value = EstadoUi.Error(
                    e.message ?: "Error al cargar recetas"
                )
            }
        }
    }

    // Marcar o desmarcar como favorita
    fun toggleFavorita(receta: Recipe) {
        viewModelScope.launch {
            try {
                guardarFavorita(receta)

                // Actualizar la lista local
                val estadoActual = _estado.value
                if (estadoActual is EstadoUi.Exito) {
                    val recetasActualizadas = estadoActual.recetas.map {
                        if (it.id == receta.id) {
                            it.copy(esFavorita = !it.esFavorita)
                        } else {
                            it
                        }
                    }
                    _estado.value = EstadoUi.Exito(recetasActualizadas)
                }
            } catch (e: Exception) {
                // Mostrar error pero no cambiar el estado principal
                println("Error al guardar favorita: ${e.message}")
            }
        }
    }

    // Guardar receta temporal para DetailScreen
    fun guardarRecetaTemporal(receta: Recipe) {
        viewModelScope.launch {
            try {
                repository.guardarRecetaTemporal(receta)
            } catch (e: Exception) {
                println("Error al guardar temporal: ${e.message}")
            }
        }
    }
}