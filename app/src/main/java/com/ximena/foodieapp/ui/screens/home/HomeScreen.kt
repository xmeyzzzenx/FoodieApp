package com.ximena.foodieapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.di.AppDependencies
import com.ximena.foodieapp.ui.components.LoadingIndicator
import com.ximena.foodieapp.ui.components.RecipeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMealPlan: () -> Unit
) {
    val context = LocalContext.current

    val viewModel = remember {
        HomeViewModel(
            getRecipes = AppDependencies.provideGetRecipesUseCase(context),
            toggleFavorite = AppDependencies.provideToggleFavoriteUseCase(context),
            recipeRepository = AppDependencies.provideRecipeRepository(context),
            apiKey = AppDependencies.getApiKey()
        )
    }

    val state by viewModel.state.collectAsState()

    var query by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas") },
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
