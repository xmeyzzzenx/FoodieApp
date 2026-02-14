package com.ximena.foodieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ximena.foodieapp.ui.screens.detail.DetailScreen
import com.ximena.foodieapp.ui.screens.favorites.FavoritesScreen
import com.ximena.foodieapp.ui.screens.form.FormScreen
import com.ximena.foodieapp.ui.screens.home.HomeScreen
import com.ximena.foodieapp.ui.screens.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Home
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { recetaId ->
                    navController.navigate("detail/$recetaId")
                },
                onNavigateToFavorites = {
                    navController.navigate("favorites")
                }
            )
        }

        // Favoritas
        composable("favorites") {
            FavoritesScreen(
                onNavigateToDetail = { recetaId ->
                    navController.navigate("detail/$recetaId")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Detalle
        composable(
            route = "detail/{recetaId}",
            arguments = listOf(navArgument("recetaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getInt("recetaId") ?: 0

            DetailScreen(
                recetaId = recetaId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Formulario
        composable(route = "form") {
            FormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSave = { titulo, minutos, porciones, descripcion ->
                    navController.popBackStack()
                }
            )
        }
    }
}