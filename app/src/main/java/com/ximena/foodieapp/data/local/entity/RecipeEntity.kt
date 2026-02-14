package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla de recetas en la base de datos
@Entity(tableName = "recetas")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val titulo: String,
    val imagen: String,
    val minutosPreparacion: Int,
    val porciones: Int,
    val descripcion: String,
    val esFavorita: Boolean = false
)