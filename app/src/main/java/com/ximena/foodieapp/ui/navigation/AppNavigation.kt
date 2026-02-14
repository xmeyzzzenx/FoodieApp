package com.ximena.foodieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.ui.screens.detail.DetailScreen
import com.ximena.foodieapp.ui.screens.favorites.FavoritesScreen
import com.ximena.foodieapp.ui.screens.form.FormScreen
import com.ximena.foodieapp.ui.screens.home.HomeScreen
import com.ximena.foodieapp.ui.screens.login.LoginScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gson = Gson()

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
                onNavigateToDetail = { receta ->
                    val recetaJson = gson.toJson(receta)
                    // CODIFICAR URL para evitar caracteres especiales
                    val recetaEncoded = URLEncoder.encode(recetaJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$recetaEncoded")
                },
                onNavigateToFavorites = {
                    navController.navigate("favorites")
                }
            )
        }

        // Favoritas
        composable("favorites") {
            FavoritesScreen(
                onNavigateToDetail = { receta ->
                    val recetaJson = gson.toJson(receta)
                    // CODIFICAR URL
                    val recetaEncoded = URLEncoder.encode(recetaJson, StandardCharsets.UTF_8.toString())
                    navController.navigate("detail/$recetaEncoded")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Detalle
        composable(
            route = "detail/{recetaJson}",
            arguments = listOf(navArgument("recetaJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val recetaEncoded = backStackEntry.arguments?.getString("recetaJson")
            // DECODIFICAR URL
            val recetaJson = URLDecoder.decode(recetaEncoded, StandardCharsets.UTF_8.toString())
            val receta = gson.fromJson(recetaJson, Recipe::class.java)

            DetailScreen(
                receta = receta,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Formulario
        composable("form") {
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