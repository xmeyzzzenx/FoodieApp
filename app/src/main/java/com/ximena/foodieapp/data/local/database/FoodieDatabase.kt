package com.ximena.foodieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.RecipeEntity

// Base de datos local (Room)
@Database(
    entities = [RecipeEntity::class, MealPlanEntity::class], // Tablas
    version = 1, // Versión de la BD
    exportSchema = false // Evita generar schema en proyecto
)
abstract class FoodieDatabase : RoomDatabase() {

    // Acceso a recetas
    abstract fun recipeDao(): RecipeDao

    // Acceso al plan semanal
    abstract fun mealPlanDao(): MealPlanDao
}