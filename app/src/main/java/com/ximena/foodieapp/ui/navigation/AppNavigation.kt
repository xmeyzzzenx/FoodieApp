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
import com.ximena.foodieapp.ui.screens.mealplan.MealPlanScreen

// Rutas de navegación (tipado para no liarla con strings sueltos)
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object MealPlan : Screen("mealplan")
    data object Form : Screen("form")

    data object Detail : Screen("detail/{recipeId}") {
        const val ARG_RECIPE_ID = "recipeId"
        fun createRoute(recipeId: Int) = "detail/$recipeId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                },
                onNavigateToMealPlan = {
                    navController.navigate(Screen.MealPlan.route)
                }
            )
        }

        // Favorites
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToDetail = { recipeId ->
                    navController.navigate(Screen.Detail.createRoute(recipeId))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Detail (✅ encaja con tu DetailScreen(recipeId, onNavigateBack))
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(Screen.Detail.ARG_RECIPE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt(Screen.Detail.ARG_RECIPE_ID) ?: 0

            DetailScreen(
                recipeId = recipeId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // MealPlan
        composable(Screen.MealPlan.route) {
            MealPlanScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Form
        composable(Screen.Form.route) {
            FormScreen(
                onNavigateBack = { navController.popBackStack() },
                onSave = { _, _, _, _ ->
                    navController.popBackStack()
                }
            )
        }
    }
}
