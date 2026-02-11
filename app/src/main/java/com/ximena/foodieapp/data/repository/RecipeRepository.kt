package com.ximena.foodieapp.data.repository

import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.entity.RecipeEntity
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Intermediario entre la API y la base de datos local
// Es el único sitio donde se decide de dónde vienen los datos
class RecipeRepository @Inject constructor(
    private val api: SpoonacularApi,  // Fuente remota
    private val dao: RecipeDao        // Fuente local
) {

    // Pide recetas a la API y las convierte a modelo interno
    suspend fun obtenerRecetas(apiKey: String): List<Recipe> {
        return api.obtenerRecetas(apiKey).results.map { dto ->
            Recipe(
                id = dto.id,
                titulo = dto.title,
                imagen = dto.image,
                minutosPreparacion = dto.readyInMinutes,
                porciones = dto.servings,
                descripcion = dto.summary
            )
        }
    }

    // Pide el detalle de una receta a la API
    suspend fun obtenerDetalleReceta(id: Int, apiKey: String): Recipe {
        val dto = api.obtenerDetalleReceta(id, apiKey)
        return Recipe(
            id = dto.id,
            titulo = dto.title,
            imagen = dto.image,
            minutosPreparacion = dto.readyInMinutes,
            porciones = dto.servings,
            descripcion = dto.summary
        )
    }

    // Devuelve las recetas favoritas guardadas localmente
    // Flow actualiza la lista automáticamente cuando hay cambios
    fun obtenerFavoritas(): Flow<List<Recipe>> {
        return dao.obtenerFavoritas().map { lista ->
            lista.map { entity ->
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

    // Guarda una receta como favorita en la base de datos local
    suspend fun guardarFavorita(recipe: Recipe) {
        dao.insertarReceta(
            RecipeEntity(
                id = recipe.id,
                titulo = recipe.titulo,
                imagen = recipe.imagen,
                minutosPreparacion = recipe.minutosPreparacion,
                porciones = recipe.porciones,
                descripcion = recipe.descripcion,
                esFavorita = true
            )
        )
    }

    // Elimina una receta de favoritas
    suspend fun eliminarFavorita(recipe: Recipe) {
        dao.eliminarReceta(
            RecipeEntity(
                id = recipe.id,
                titulo = recipe.titulo,
                imagen = recipe.imagen,
                minutosPreparacion = recipe.minutosPreparacion,
                porciones = recipe.porciones,
                descripcion = recipe.descripcion,
                esFavorita = false
            )
        )
    }

    // Busca recetas por título en la base de datos local
    fun buscarRecetas(busqueda: String): Flow<List<Recipe>> {
        return dao.buscarPorTitulo(busqueda).map { lista ->
            lista.map { entity ->
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