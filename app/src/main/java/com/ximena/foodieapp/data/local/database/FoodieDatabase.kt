package com.ximena.foodieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.RecipeEntity

// Base de datos principal de la app
@Database(
    entities = [RecipeEntity::class, MealPlanEntity::class],
    version = 1
)
abstract class FoodieDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
}