package com.ximena.foodieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ximena.foodieapp.ui.screens.*
import com.ximena.foodieapp.ui.viewmodel.AuthViewModel

// Rutas de la app: cada objeto es una pantalla con su ruta de navegación
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object MealPlan : Screen("meal_plan")
    object MyRecipes : Screen("my_recipes")
    object ShoppingList : Screen("shopping_list")
    // RecipeForm acepta recipeId opcional (null = nueva, con valor = editar)
    object RecipeForm : Screen("recipe_form?recipeId={recipeId}") {
        fun createRoute(recipeId: String? = null) =
            if (recipeId != null) "recipe_form?recipeId=$recipeId" else "recipe_form"
    }
    // RecipeDetail requiere mealId obligatorio en la ruta
    object RecipeDetail : Screen("recipe_detail/{mealId}") {
        fun createRoute(mealId: String) = "recipe_detail/$mealId"
    }
}

@Composable
fun FoodieNavHost(navController: NavHostController = rememberNavController()) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    // Arranca en Home si ya hay sesión, o en Login si no
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // popUpTo con inclusive = true elimina Login del backstack (no se puede volver)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onRecipeClick = { navController.navigate(Screen.RecipeDetail.createRoute(it)) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites.route) },
                onNavigateToMealPlan = { navController.navigate(Screen.MealPlan.route) },
                onNavigateToMyRecipes = { navController.navigate(Screen.MyRecipes.route) },
                onNavigateToShopping = { navController.navigate(Screen.ShoppingList.route) },
                onLogout = {
                    // popUpTo(0) limpia todo el backstack, no queda nada atrás
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onRecipeClick = { navController.navigate(Screen.RecipeDetail.createRoute(it)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onRecipeClick = { navController.navigate(Screen.RecipeDetail.createRoute(it)) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MealPlan.route) {
            MealPlanScreen(
                onBack = { navController.popBackStack() },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(Screen.MyRecipes.route) {
            MyRecipesScreen(
                onRecipeClick = { navController.navigate(Screen.RecipeDetail.createRoute(it)) },
                onAddRecipe = { navController.navigate(Screen.RecipeForm.createRoute()) },
                onEditRecipe = { navController.navigate(Screen.RecipeForm.createRoute(it)) },
                onBack = { navController.popBackStack() }
            )
        }

        // RecipeForm: recipeId es nullable y opcional en la URL
        composable(
            route = Screen.RecipeForm.route,
            arguments = listOf(navArgument("recipeId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) {
            RecipeFormScreen(
                recipeId = it.arguments?.getString("recipeId"),
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.ShoppingList.route) {
            ShoppingListScreen(onBack = { navController.popBackStack() })
        }

        // RecipeDetail: mealId obligatorio, si no viene se cancela la navegación
        composable(
            route = Screen.RecipeDetail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) {
            val mealId = it.arguments?.getString("mealId") ?: return@composable
            RecipeDetailScreen(
                mealId = mealId,
                onBack = { navController.popBackStack() },
                onAddToMealPlan = { navController.navigate(Screen.MealPlan.route) },
                onAddToShopping = { navController.navigate(Screen.ShoppingList.route) }
            )
        }
    }
}