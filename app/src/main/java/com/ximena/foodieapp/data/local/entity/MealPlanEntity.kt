package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plan_comidas")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) // Room genera el ID automáticamente
    val id: Int = 0,
    val diaSemana: String,
    val tipoComida: String,
    val recetaId: Int,
    val tituloReceta: String
)