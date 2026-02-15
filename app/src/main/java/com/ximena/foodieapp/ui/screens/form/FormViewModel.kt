package com.ximena.foodieapp.ui.screens.form

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun onTituloChange(value: String) {
        _uiState.value = _uiState.value.copy(
            titulo = value,
            errorTitulo = false
        )
    }

    fun onMinutosChange(value: String) {
        _uiState.value = _uiState.value.copy(
            minutos = value.filter { it.isDigit() },
            errorMinutos = false
        )
    }

    fun onPorcionesChange(value: String) {
        _uiState.value = _uiState.value.copy(
            porciones = value.filter { it.isDigit() },
            errorPorciones = false
        )
    }

    fun onDescripcionChange(value: String) {
        _uiState.value = _uiState.value.copy(
            descripcion = value,
            errorDescripcion = false
        )
    }

    fun validar(): Boolean {
        val estado = _uiState.value

        val errorTitulo = estado.titulo.isBlank()
        val errorMinutos = estado.minutos.isBlank() || estado.minutos.toIntOrNull() == null
        val errorPorciones = estado.porciones.isBlank() || estado.porciones.toIntOrNull() == null
        val errorDescripcion = estado.descripcion.isBlank()

        _uiState.value = estado.copy(
            errorTitulo = errorTitulo,
            errorMinutos = errorMinutos,
            errorPorciones = errorPorciones,
            errorDescripcion = errorDescripcion
        )

        return !(errorTitulo || errorMinutos || errorPorciones || errorDescripcion)
    }

    fun limpiar() {
        _uiState.value = FormUiState()
    }
}
