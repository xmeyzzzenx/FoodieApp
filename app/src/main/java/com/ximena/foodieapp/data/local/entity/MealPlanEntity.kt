package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "plan_comidas" en la base de datos local
@Entity(tableName = "plan_comidas")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val diaSemana: String,
    val tipoComida: String,
    val recetaId: Int,
    val tituloReceta: String
)