package com.ximena.foodieapp.ui.screens.form

data class FormUiState(
    val titulo: String = "",
    val minutos: String = "",
    val porciones: String = "",
    val descripcion: String = "",

    val errorTitulo: Boolean = false,
    val errorMinutos: Boolean = false,
    val errorPorciones: Boolean = false,
    val errorDescripcion: Boolean = false
)
