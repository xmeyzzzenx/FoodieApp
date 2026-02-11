package com.ximena.foodieapp.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ximena.foodieapp.ui.screens.*

// Rutas de navegación
object Rutas {
    const val LOGIN = "login"
    const val HOME = "home"
    const val DETALLE = "detalle/{recetaId}"
    const val FAVORITAS = "favoritas"

    // Genera la ruta de detalle con el id real
    fun detalle(id: Int) = "detalle/$id"
}

// Gestiona la navegación entre pantallas
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {

        // Pantalla de login
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLogin = { navController.navigate(Rutas.HOME) }
            )
        }

        // Pantalla principal
        composable(Rutas.HOME) {
            HomeScreen(
                recipes = emptyList(),
                onRecipeClick = { recipe ->
                    navController.navigate(Rutas.detalle(recipe.id))
                },
                onFavoriteClick = {},
                onSearch = {}
            )
        }

        // Pantalla de detalle
        composable(Rutas.DETALLE) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("recetaId")?.toInt()
            DetailScreen(
                recipe = null,
                onFavoriteClick = {},
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de favoritas
        composable(Rutas.FAVORITAS) {
            FavoritesScreen(
                favorites = emptyList(),
                onRecipeClick = { recipe ->
                    navController.navigate(Rutas.detalle(recipe.id))
                }
            )
        }
    }
}