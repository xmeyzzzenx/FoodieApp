package com.ximena.foodieapp.domain.model

// Modelo interno de un registro del plan semanal
data class MealPlan(
    val id: Int,
    val diaSemana: String,
    val tipoComida: String,
    val recetaId: Int,
    val tituloReceta: String
)