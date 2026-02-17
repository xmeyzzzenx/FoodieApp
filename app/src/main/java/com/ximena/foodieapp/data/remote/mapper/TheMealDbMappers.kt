package com.ximena.foodieapp.data.remote.mapper

import com.ximena.foodieapp.data.remote.dto.TheMealDbMealDto
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.domain.model.RecipeIngredient

fun TheMealDbMealDto.toRecipe(): Recipe {
    return Recipe(
        id = idMeal?.toIntOrNull() ?: 0,
        title = strMeal.orEmpty(),
        image = strMealThumb,
        category = strCategory,
        area = strArea
    )
}

fun TheMealDbMealDto.toRecipeDetail(): RecipeDetail {
    return RecipeDetail(
        id = idMeal?.toIntOrNull() ?: 0,
        title = strMeal.orEmpty(),
        image = strMealThumb,
        readyInMinutes = null,
        servings = null,
        ingredients = buildIngredients(),
        instructions = strInstructions.orEmpty()
    )
}

private fun TheMealDbMealDto.buildIngredients(): List<RecipeIngredient> {
    val pairs = listOf(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16,
        strIngredient17 to strMeasure17,
        strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19,
        strIngredient20 to strMeasure20
    )

    return pairs.mapNotNull { (ingredient, measure) ->
        val name = ingredient?.trim().orEmpty()
        if (name.isBlank()) return@mapNotNull null

        val qty = measure?.trim().orEmpty()
        RecipeIngredient(
            name = name,
            original = if (qty.isBlank()) name else "$qty $name"
        )
    }
}
