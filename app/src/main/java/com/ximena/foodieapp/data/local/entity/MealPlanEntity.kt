package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla del plan semanal en la base de datos
@Entity(tableName = "plan_semanal")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val diaSemana: String,
    val tipoComida: String,
    val recetaId: Int,
    val tituloReceta: String
)