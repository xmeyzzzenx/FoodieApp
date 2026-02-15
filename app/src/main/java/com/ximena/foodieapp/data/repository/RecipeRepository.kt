package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.data.remote.dto.RecipeDto
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Repositorio de recetas: conecta API (Spoonacular) + BD local (Room)
class RecipeRepository(
    private val api: SpoonacularApi,
    private val dao: RecipeDao
) {

    // ─────────────────────────────────────────────────────────────────────
    // API (Spoonacular)
    // ─────────────────────────────────────────────────────────────────────

    // Buscar / listar recetas desde la API
    suspend fun fetchRecipes(apiKey: String, query: String? = null): List<Recipe> {
        val response = api.searchRecipes(apiKey = apiKey, query = query)
        return response.results.map { it.toDomainFromApi() }
    }

    // Obtener detalle de receta por ID desde la API
    suspend fun fetchRecipeById(apiKey: String, recipeId: Int): Recipe {
        return api.getRecipeInformation(id = recipeId, apiKey = apiKey).toDomainFromApi()
    }

    // ─────────────────────────────────────────────────────────────────────
    // ROOM (favoritas guardadas)
    // ─────────────────────────────────────────────────────────────────────

    // Obtener favoritas (Room)
    fun getFavorites(): Flow<List<Recipe>> =
        dao.getFavorites().map { entities: List<RecipeEntity> ->
            entities.map { it.toDomainFromDb() }
        }

    // Buscar dentro de favoritas (Room)
    fun searchFavorites(query: String): Flow<List<Recipe>> =
        dao.search(query).map { entities: List<RecipeEntity> ->
            entities.map { it.toDomainFromDb() }
        }

    // Obtener receta guardada por ID (Room)
    fun getById(recipeId: Int): Flow<Recipe?> =
        dao.getById(recipeId).map { entity ->
            entity?.toDomainFromDb()
        }

    // Guardar/actualizar receta en Room (insert con REPLACE)
    suspend fun save(recipe: Recipe) {
        dao.insert(recipe.toEntity())
    }

    // Eliminar receta de Room
    suspend fun delete(recipe: Recipe) {
        dao.delete(recipe.toEntity())
    }

    // Marcar/desmarcar favorita (Room)
    suspend fun setFavorite(recipe: Recipe, isFavorite: Boolean) {
        dao.insert(recipe.copy(isFavorite = isFavorite).toEntity())
    }

    // ─────────────────────────────────────────────────────────────────────
    // MAPPERS (privados)
    // ─────────────────────────────────────────────────────────────────────

    // API DTO -> Domain
    private fun RecipeDto.toDomainFromApi(): Recipe = Recipe(
        id = id,
        title = title,
        imageUrl = image,
        readyInMinutes = readyInMinutes,
        servings = servings,
        summary = summary,
        isFavorite = false // la API no sabe tus favoritas
    )

    // DB Entity -> Domain
    private fun RecipeEntity.toDomainFromDb(): Recipe = Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        readyInMinutes = readyInMinutes,
        servings = servings,
        summary = summary,
        isFavorite = isFavorite
    )

    // Domain -> DB Entity
    private fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        readyInMinutes = readyInMinutes,
        servings = servings,
        summary = summary,
        isFavorite = isFavorite
    )
}
