package com.ximena.foodieapp.data.remote.mapper

import com.ximena.foodieapp.data.local.entity.RecipeEntity
import com.ximena.foodieapp.data.remote.dto.CategoryDto
import com.ximena.foodieapp.data.remote.dto.MealDetailDto
import com.ximena.foodieapp.data.remote.dto.MealDto
import com.ximena.foodieapp.domain.model.Category
import com.ximena.foodieapp.domain.model.MealSummary
import com.ximena.foodieapp.domain.model.Recipe

// DTO (API) → Model resumido para listas
fun MealDto.toMealSummary() = MealSummary(
    id = idMeal,
    name = strMeal,
    thumbnailUrl = strMealThumb
)

// DTO (API) → Model de categoría
fun CategoryDto.toCategory() = Category(
    id = idCategory,
    name = strCategory,
    thumbnailUrl = strCategoryThumb,
    description = strCategoryDescription
)

// DTO (API) → Model completo de receta
fun MealDetailDto.toRecipe(isFavorite: Boolean = false): Recipe {
    val ingredients = getIngredientsList()
    val measures = getMeasuresList()
    return Recipe(
        id = idMeal,
        name = strMeal,
        category = strCategory,
        area = strArea,
        instructions = strInstructions,
        thumbnailUrl = strMealThumb,
        // Las tags vienen como "Pasta,Baked" → se separan en lista y se limpian
        tags = strTags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
        youtubeUrl = strYoutube,
        ingredients = ingredients,
        measures = measures,
        isFavorite = isFavorite
    )
}

// DTO (API) → Entity de Room (para cachear la receta localmente)
// Los ingredientes se guardan como "harina||azúcar||huevo" separados por ||
fun MealDetailDto.toRecipeEntity(userId: String, isFavorite: Boolean = false): RecipeEntity {
    val ingredients = getIngredientsList()
    val measures = getMeasuresList()
    return RecipeEntity(
        id = idMeal,
        userId = userId,
        name = strMeal,
        category = strCategory,
        area = strArea,
        instructions = strInstructions,
        thumbnailUrl = strMealThumb,
        tags = strTags,
        youtubeUrl = strYoutube,
        ingredients = ingredients.joinToString("||"),
        measures = measures.joinToString("||"),
        isFavorite = isFavorite
    )
}

// Entity (Room) → Model (para mostrar en UI)
// Se divide el string "harina||azúcar" de vuelta a lista
fun RecipeEntity.toRecipe() = Recipe(
    id = id,
    name = name,
    category = category,
    area = area,
    instructions = instructions,
    thumbnailUrl = thumbnailUrl,
    tags = tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList(),
    youtubeUrl = youtubeUrl,
    ingredients = if (ingredients.isBlank()) emptyList() else ingredients.split("||"),
    measures = if (measures.isBlank()) emptyList() else measures.split("||"),
    isFavorite = isFavorite,
    isUserCreated = isUserCreated
)

// Model → Entity (para guardar recetas creadas por el usuario)
fun Recipe.toEntity(userId: String) = RecipeEntity(
    id = id,
    userId = userId,
    name = name,
    category = category,
    area = area,
    instructions = instructions,
    thumbnailUrl = thumbnailUrl,
    tags = tags.joinToString(","),
    youtubeUrl = youtubeUrl,
    ingredients = ingredients.joinToString("||"),
    measures = measures.joinToString("||"),
    isFavorite = isFavorite,
    isUserCreated = isUserCreated
)