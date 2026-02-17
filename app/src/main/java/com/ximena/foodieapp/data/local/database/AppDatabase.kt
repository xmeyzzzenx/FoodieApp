package com.ximena.foodieapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ximena.foodieapp.data.local.dao.FavoriteDao
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.UserRecipeDao
import com.ximena.foodieapp.data.local.entity.FavoriteRecipeEntity
import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity

@Database(
    entities = [
        FavoriteRecipeEntity::class,
        MealPlanEntity::class,
        UserRecipeEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun userRecipeDao(): UserRecipeDao
}
