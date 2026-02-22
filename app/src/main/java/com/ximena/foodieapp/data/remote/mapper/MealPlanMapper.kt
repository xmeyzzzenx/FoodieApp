package com.ximena.foodieapp.data.remote.mapper

import com.ximena.foodieapp.data.local.entity.MealPlanEntity
import com.ximena.foodieapp.data.local.entity.ShoppingItemEntity
import com.ximena.foodieapp.domain.model.DayOfWeek
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.domain.model.MealType
import com.ximena.foodieapp.domain.model.ShoppingItem

fun MealPlanEntity.toMealPlan() = MealPlan(
    id = id,
    recipeId = recipeId,
    recipeName = recipeName,
    recipeThumbnail = recipeThumbnail,
    dayOfWeek = DayOfWeek.valueOf(dayOfWeek),
    mealType = MealType.valueOf(mealType),
    weekYear = weekYear
)

fun MealPlan.toEntity() = MealPlanEntity(
    id = id,
    recipeId = recipeId,
    recipeName = recipeName,
    recipeThumbnail = recipeThumbnail,
    dayOfWeek = dayOfWeek.name,
    mealType = mealType.name,
    weekYear = weekYear
)

fun ShoppingItemEntity.toShoppingItem() = ShoppingItem(
    id = id,
    name = name,
    measure = measure,
    isChecked = isChecked,
    recipeId = recipeId,
    recipeName = recipeName
)

fun ShoppingItem.toEntity() = ShoppingItemEntity(
    id = id,
    name = name,
    measure = measure,
    isChecked = isChecked,
    recipeId = recipeId,
    recipeName = recipeName
)
