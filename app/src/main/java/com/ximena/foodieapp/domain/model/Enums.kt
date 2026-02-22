package com.ximena.foodieapp.domain.model

// Enum de días de la semana con su nombre en español para mostrar en la UI
enum class DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    fun displayName(): String = when (this) {
        MONDAY -> "Lunes"
        TUESDAY -> "Martes"
        WEDNESDAY -> "Miércoles"
        THURSDAY -> "Jueves"
        FRIDAY -> "Viernes"
        SATURDAY -> "Sábado"
        SUNDAY -> "Domingo"
    }
}

// Enum de tipos de comida con su nombre en español
enum class MealType {
    BREAKFAST, LUNCH, DINNER;

    fun displayName(): String = when (this) {
        BREAKFAST -> "Desayuno"
        LUNCH -> "Comida"
        DINNER -> "Cena"
    }
}