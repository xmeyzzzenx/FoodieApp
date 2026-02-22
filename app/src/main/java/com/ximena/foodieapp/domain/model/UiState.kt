package com.ximena.foodieapp.domain.model

// Clase sellada para representar los 3 estados posibles de cualquier pantalla
// sealed = solo puede ser uno de estos tres tipos, nada más
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()           // Cargando datos
    data class Success<T>(val data: T) : UiState<T>() // Datos listos
    data class Error(val message: String) : UiState<Nothing>() // Algo fue mal
}