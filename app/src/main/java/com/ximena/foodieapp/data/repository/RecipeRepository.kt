package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Intermediario entre la API y la base de datos
class RecipeRepository(
    private val api: SpoonacularApi,
    private val dao: RecipeDao
) {

    // Obtener recetas de la API
    suspend fun obtenerRecetas(apiKey: String, busqueda: String? = null): List<Recipe> {
        val response = api.obtenerRecetas(apiKey, busqueda)
        return response.results.map { dto ->
            Recipe(
                id = dto.id,
                titulo = dto.title,
                imagen = dto.image,
                minutosPreparacion = dto.readyInMinutes,
                porciones = dto.servings,
                descripcion = dto.summary,
                esFavorita = false
            )
        }
    }

    // Guardar receta como favorita
    suspend fun guardarFavorita(recipe: Recipe) {
        val entity = RecipeEntity(
            id = recipe.id,
            titulo = recipe.titulo,
            imagen = recipe.imagen,
            minutosPreparacion = recipe.minutosPreparacion,
            porciones = recipe.porciones,
            descripcion = recipe.descripcion,
            esFavorita = true
        )
        dao.insertar(entity)
    }

    // Obtener favoritas de Room
    fun obtenerFavoritas(): Flow<List<Recipe>> {
        return dao.obtenerFavoritas().map { entities ->
            entities.map { entity ->
                Recipe(
                    id = entity.id,
                    titulo = entity.titulo,
                    imagen = entity.imagen,
                    minutosPreparacion = entity.minutosPreparacion,
                    porciones = entity.porciones,
                    descripcion = entity.descripcion,
                    esFavorita = entity.esFavorita
                )
            }
        }
    }

    // Actualizar receta
    suspend fun actualizarReceta(recipe: Recipe) {
        val entity = RecipeEntity(
            id = recipe.id,
            titulo = recipe.titulo,
            imagen = recipe.imagen,
            minutosPreparacion = recipe.minutosPreparacion,
            porciones = recipe.porciones,
            descripcion = recipe.descripcion,
            esFavorita = recipe.esFavorita
        )
        dao.actualizar(entity)
    }

    // Eliminar receta de favoritas
    suspend fun eliminarFavorita(recipe: Recipe) {
        val entity = RecipeEntity(
            id = recipe.id,
            titulo = recipe.titulo,
            imagen = recipe.imagen,
            minutosPreparacion = recipe.minutosPreparacion,
            porciones = recipe.porciones,
            descripcion = recipe.descripcion,
            esFavorita = false
        )
        dao.eliminar(entity)
    }

    // Buscar en favoritas locales
    fun buscarEnFavoritas(busqueda: String): Flow<List<Recipe>> {
        return dao.buscarPorTitulo(busqueda).map { entities ->
            entities.map { entity ->
                Recipe(
                    id = entity.id,
                    titulo = entity.titulo,
                    imagen = entity.imagen,
                    minutosPreparacion = entity.minutosPreparacion,
                    porciones = entity.porciones,
                    descripcion = entity.descripcion,
                    esFavorita = entity.esFavorita
                )
            }
        }
    }
}