package com.ximena.foodieapp.ui.screens.home

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.data.auth.AuthManager
import com.ximena.foodieapp.di.AppDependencies
import com.ximena.foodieapp.ui.components.LoadingIndicator
import com.ximena.foodieapp.ui.components.RecipeCard
import com.ximena.foodieapp.ui.screens.login.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMealPlan: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    // ViewModel de recetas (el tuyo)
    val viewModel = remember {
        HomeViewModel(
            getRecipes = AppDependencies.provideGetRecipesUseCase(context),
            toggleFavorite = AppDependencies.provideToggleFavoriteUseCase(context),
            recipeRepository = AppDependencies.provideRecipeRepository(context),
            apiKey = AppDependencies.getApiKey()
        )
    }
    val state by viewModel.state.collectAsState()

    // ViewModel Auth0 (para perfil + logout)
    val authVm = remember { AuthViewModel(AuthManager(context)) }
    val authState by authVm.state.collectAsState()

    // Cargar perfil al entrar (para enseñar email/nombre)
    LaunchedEffect(Unit) { authVm.checkSession() }

    var query by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    // Nombre/email (si Auth0 lo trae)
    val userLabel = when (val s = authState) {
        is AuthViewModel.UiState.LoggedIn -> {
            val email = s.profile?.email
            val name = s.profile?.name
            name ?: email ?: "Usuario"
        }
        else -> "Usuario"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas • $userLabel") },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favoritas")
                    }
                    IconButton(onClick = onNavigateToMealPlan) {
                        Icon(Icons.Default.DateRange, contentDescription = "Plan semanal")
                    }

                    // ✅ Logout real Auth0
                    IconButton(onClick = {
                        // lo lanzamos y nos vamos a Login (popUpTo ya lo hace Navigation)
                        authVm.logout(activity)
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (showSearch) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar recetas...") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.loadRecipes(query.ifBlank { null }) }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    }
                )
            }

            when (val s = state) {
                is HomeViewModel.UiState.Loading -> {
                    LoadingIndicator()
                }

                is HomeViewModel.UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = s.recipes,
                            key = { it.id }
                        ) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = {
                                    viewModel.saveTempForDetail(recipe)
                                    onNavigateToDetail(recipe.id)
                                },
                                onFavoriteClick = { viewModel.onToggleFavorite(recipe) }
                            )
                        }
                    }
                }

                is HomeViewModel.UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = s.message,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(onClick = { viewModel.loadRecipes() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
