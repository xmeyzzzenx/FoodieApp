package com.ximena.foodieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ximena.foodieapp.ui.screens.detail.RecipeDetailScreen
import com.ximena.foodieapp.ui.screens.explore.ExploreScreen
import com.ximena.foodieapp.ui.screens.favorites.FavoritesScreen
import com.ximena.foodieapp.ui.screens.login.LoginScreen
import com.ximena.foodieapp.ui.screens.myrecipes.MyRecipesScreen
import com.ximena.foodieapp.ui.screens.picklocal.PickLocalScreen
import com.ximena.foodieapp.ui.screens.pickonline.PickOnlineScreen
import com.ximena.foodieapp.ui.screens.planner.MealPlannerScreen
import com.ximena.foodieapp.ui.screens.planner.MealPlannerViewModel
import com.ximena.foodieapp.ui.screens.recipeform.RecipeFormScreen
import com.ximena.foodieapp.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash,
        modifier = modifier
    ) {
        composable(Routes.Splash) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                },
                onNavigateToExplore = {
                    navController.navigate(Routes.Explore) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Explore) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Explore) {
            ExploreScreen(
                onOpenRecipe = { recipeId, isOnline ->
                    navController.navigate("${Routes.Detail}/$recipeId/$isOnline")
                },
                onOpenFavorites = { navController.navigate(Routes.Favorites) },
                onOpenPlanner = { navController.navigate(Routes.Planner) },
                onOpenMyRecipes = { navController.navigate(Routes.MyRecipes) },
                onLogout = {
                    navController.navigate(Routes.Login) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(
            route = "${Routes.Detail}/{${Routes.ArgRecipeId}}/{${Routes.ArgIsOnline}}",
            arguments = listOf(
                navArgument(Routes.ArgRecipeId) { type = NavType.IntType },
                navArgument(Routes.ArgIsOnline) { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt(Routes.ArgRecipeId) ?: 0
            val isOnline = backStackEntry.arguments?.getBoolean(Routes.ArgIsOnline) ?: true

            RecipeDetailScreen(
                recipeId = recipeId,
                isOnline = isOnline,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Favorites) {
            FavoritesScreen(
                onOpenRecipe = { recipeId ->
                    navController.navigate("${Routes.Detail}/$recipeId/false")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Planner) {
            MealPlannerScreen(
                onPickOnline = { day, mealType ->
                    navController.navigate("${Routes.PickOnline}/$day/$mealType")
                },
                onPickLocal = { day, mealType ->
                    navController.navigate("${Routes.PickLocal}/$day/$mealType")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.PickOnline}/{${Routes.ArgDay}}/{${Routes.ArgMealType}}",
            arguments = listOf(
                navArgument(Routes.ArgDay) { type = NavType.StringType },
                navArgument(Routes.ArgMealType) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val day = backStackEntry.arguments?.getString(Routes.ArgDay) ?: "Monday"
            val mealType = backStackEntry.arguments?.getString(Routes.ArgMealType) ?: "Breakfast"
            val plannerVm: MealPlannerViewModel = hiltViewModel(navController.getBackStackEntry(Routes.Planner))

            PickOnlineScreen(
                day = day,
                mealType = mealType,
                onPick = { recipeId, title, image ->
                    plannerVm.setSlotOnline(day, mealType, recipeId, title, image)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.PickLocal}/{${Routes.ArgDay}}/{${Routes.ArgMealType}}",
            arguments = listOf(
                navArgument(Routes.ArgDay) { type = NavType.StringType },
                navArgument(Routes.ArgMealType) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val day = backStackEntry.arguments?.getString(Routes.ArgDay) ?: "Monday"
            val mealType = backStackEntry.arguments?.getString(Routes.ArgMealType) ?: "Breakfast"
            val plannerVm: MealPlannerViewModel = hiltViewModel(navController.getBackStackEntry(Routes.Planner))

            PickLocalScreen(
                day = day,
                mealType = mealType,
                onPick = { localId, title, image ->
                    plannerVm.setSlotLocal(day, mealType, localId, title, image)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.MyRecipes) {
            MyRecipesScreen(
                onAddNew = { navController.navigate("${Routes.RecipeForm}/0") },
                onEdit = { localId -> navController.navigate("${Routes.RecipeForm}/$localId") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Routes.RecipeForm}/{localId}",
            arguments = listOf(
                navArgument("localId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getLong("localId") ?: 0L

            RecipeFormScreen(
                localId = localId,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
