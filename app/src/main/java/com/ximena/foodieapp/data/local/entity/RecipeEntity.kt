package com.ximena.foodieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recetas")
data class RecipeEntity(
    @PrimaryKey // Esta columna es la clave primaria (identificador único)
    val id: Int,
    val titulo: String,
    val imagen: String,
    val minutosPreparacion: Int,
    val porciones: Int,
    val descripcion: String,
    val esFavorita: Boolean = false
)