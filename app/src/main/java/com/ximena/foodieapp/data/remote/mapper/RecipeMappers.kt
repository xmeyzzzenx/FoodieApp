package com.ximena.foodieapp.data.remote.mapper

import com.ximena.foodieapp.data.remote.dto.RecipeDetailDto
import com.ximena.foodieapp.data.remote.dto.RecipeItemDto
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.domain.model.RecipeIngredient

fun RecipeItemDto.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        image = image,
        readyInMinutes = readyInMinutes,
        servings = servings
    )
}

fun RecipeDetailDto.toDomain(): RecipeDetail {
    return RecipeDetail(
        id = id,
        title = title,
        image = image,
        readyInMinutes = readyInMinutes,
        servings = servings,
        ingredients = extendedIngredients.map {
            RecipeIngredient(
                name = it.name ?: "",
                original = it.original ?: ""
            )
        },
        instructions = instructions ?: ""
    )
}
