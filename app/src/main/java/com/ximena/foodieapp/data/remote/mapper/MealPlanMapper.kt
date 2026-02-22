package com.ximena.foodieapp.data.remote.mapper

import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.ShoppingItemEntity
import com.ximena.foodieapp.domain.model.DayOfWeek
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.MealType
import com.ximena.foodieapp.domain.model.ShoppingItem

// Mappers: convierten entre Entity (Room) y Model (dominio)
// Se usan para no mezclar la capa de datos con la lógica de la app

// Entity → Model (para mostrar en la UI)
fun MealPlanEntity.toMealPlan() = MealPlan(
    id = id,
    recipeId = recipeId,
    recipeName = recipeName,
    recipeThumbnail = recipeThumbnail,
    dayOfWeek = DayOfWeek.valueOf(dayOfWeek),  // String "MONDAY" → enum DayOfWeek
    mealType = MealType.valueOf(mealType),      // String "LUNCH" → enum MealType
    weekYear = weekYear
)

// Model → Entity (para guardar en Room)
fun MealPlan.toEntity(userId: String) = MealPlanEntity(
    id = id,
    userId = userId,
    recipeId = recipeId,
    recipeName = recipeName,
    recipeThumbnail = recipeThumbnail,
    dayOfWeek = dayOfWeek.name,   // Enum → String para guardarlo en BD
    mealType = mealType.name,
    weekYear = weekYear
)

// Entity → Model
fun ShoppingItemEntity.toShoppingItem() = ShoppingItem(
    id = id,
    name = name,
    measure = measure,
    isChecked = isChecked,
    recipeId = recipeId,
    recipeName = recipeName
)

// Model → Entity
fun ShoppingItem.toEntity(userId: String) = ShoppingItemEntity(
    id = id,
    userId = userId,
    name = name,
    measure = measure,
    isChecked = isChecked,
    recipeId = recipeId,
    recipeName = recipeName
)