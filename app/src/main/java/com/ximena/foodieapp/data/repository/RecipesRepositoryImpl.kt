package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.remote.api.TheMealDbApi
import com.ximena.foodieapp.data.remote.dto.TheMealDbMealDto
import com.ximena.foodieapp.data.remote.mapper.toRecipe
import com.ximena.foodieapp.data.remote.mapper.toRecipeDetail
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.domain.repository.RecipesRepository
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val api: TheMealDbApi
) : RecipesRepository {

    override suspend fun searchRecipes(query: String?): List<Recipe> {
        val q = query?.trim().orEmpty()

        val meals: List<TheMealDbMealDto> = if (q.isBlank()) {
            // 🔥 Si no hay búsqueda: traemos 10 recetas random
            val randomMeals = buildList {
                repeat(10) {
                    addAll(api.getRandomMeal().meals.orEmpty())
                }
            }

            randomMeals.distinctBy { it.idMeal }
        } else {
            // 🔍 Si hay búsqueda: usamos el search normal
            api.searchMeals(query = q).meals.orEmpty()
        }

        return meals.map { it.toRecipe() }
    }

    override suspend fun getRecipeDetail(recipeId: Int): RecipeDetail {
        val meal = api.getMealDetail(id = recipeId.toString()).meals?.firstOrNull()

        return meal?.toRecipeDetail()
            ?: RecipeDetail(
                id = recipeId,
                title = "No encontrado",
                image = null,
                readyInMinutes = null,
                servings = null,
                ingredients = emptyList(),
                instructions = ""
            )
    }
}
