package com.ximena.foodieapp.domain.model

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

enum class MealType {
    BREAKFAST, LUNCH, DINNER;

    fun displayName(): String = when (this) {
        BREAKFAST -> "Desayuno"
        LUNCH -> "Comida"
        DINNER -> "Cena"
    }
}
