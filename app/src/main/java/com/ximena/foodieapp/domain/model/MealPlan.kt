package com.ximena.foodieapp.domain.model

data class MealPlan(
    val id: Int,
    val diaSemana: String,      // Ej: "Lunes", "Martes"...
    val tipoComida: String,     // Ej: "Desayuno", "Comida", "Cena"
    val recetaId: Int,          // Qué receta está asignada a ese slot
    val tituloReceta: String    // Nombre de la receta (para mostrarlo sin consultar)
)