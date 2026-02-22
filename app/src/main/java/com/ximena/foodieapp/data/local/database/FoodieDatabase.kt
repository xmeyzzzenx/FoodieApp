package com.ximena.foodieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.dao.ShoppingItemDao
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import com.ximena.foodieapp.data.local.entity.ShoppingItemEntity

@Database(
    entities = [RecipeEntity::class, MealPlanEntity::class, ShoppingItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FoodieDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun shoppingItemDao(): ShoppingItemDao
}
