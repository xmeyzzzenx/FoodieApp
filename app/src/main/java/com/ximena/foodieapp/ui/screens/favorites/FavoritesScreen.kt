package com.ximena.foodieapp.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun FavoritesScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val viewModel = remember {
        FavoritesViewModel(
            getFavorites = AppDependencies.provideGetFavoritesUseCase(context),
            searchFavorites = AppDependencies.provideSearchFavoritesUseCase(context),
            toggleFavorite = AppDependencies.provideToggleFavoriteUseCase(context)
        )
    }

    val state by viewModel.state.collectAsState()

    var query by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
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
                    onValueChange = {
                        query = it
                        viewModel.onSearch(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar en favoritas...") },
                    singleLine = true
                )
            }

            when (val s = state) {
                is FavoritesViewModel.UiState.Loading -> {
                    LoadingIndicator()
                }

                is FavoritesViewModel.UiState.Success -> {
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
                                onClick = { onNavigateToDetail(recipe.id) },
                                onFavoriteClick = { viewModel.onToggleFavorite(recipe) }
                            )
                        }
                    }
                }

                is FavoritesViewModel.UiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (query.isBlank()) "No tienes favoritas guardadas"
                                else "No se encontraron recetas",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            if (query.isNotBlank()) {
                                Button(onClick = {
                                    query = ""
                                    viewModel.onSearch("")
                                }) {
                                    Text("Limpiar búsqueda")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
