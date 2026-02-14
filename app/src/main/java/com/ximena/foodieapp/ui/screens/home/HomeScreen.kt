package com.ximena.foodieapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
    onNavigateToFavorites: () -> Unit
) {
    val context = LocalContext.current

    // Crear ViewModel
    val viewModel = remember {
        HomeViewModel(
            obtenerRecetas = AppDependencies.provideGetRecipesUseCase(context),
            guardarFavorita = AppDependencies.provideSaveFavoriteUseCase(context),
            repository = AppDependencies.provideRecipeRepository(context),
            apiKey = AppDependencies.getApiKey()
        )
    }

    // Observar el estado
    val estado by viewModel.estado.collectAsState()

    // Estado de la búsqueda
    var textoBusqueda by remember { mutableStateOf("") }
    var mostrarBusqueda by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas") },
                actions = {
                    // Botón de búsqueda
                    IconButton(onClick = { mostrarBusqueda = !mostrarBusqueda }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }

                    // Botón de favoritas
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favoritas")
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
            // Barra de búsqueda
            if (mostrarBusqueda) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar recetas...") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.cargarRecetas(textoBusqueda.ifBlank { null })
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar")
                        }
                    }
                )
            }

            // Contenido según el estado
            when (val estadoActual = estado) {
                is HomeViewModel.EstadoUi.Cargando -> {
                    LoadingIndicator()
                }

                is HomeViewModel.EstadoUi.Exito -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estadoActual.recetas) { receta ->
                            RecipeCard(
                                recipe = receta,
                                onClick = {
                                    viewModel.guardarRecetaTemporal(receta)
                                    onNavigateToDetail(receta.id)
                                },
                                onFavoriteClick = { viewModel.toggleFavorita(receta) }
                            )
                        }
                    }
                }

                is HomeViewModel.EstadoUi.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = estadoActual.mensaje,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(onClick = { viewModel.cargarRecetas() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}