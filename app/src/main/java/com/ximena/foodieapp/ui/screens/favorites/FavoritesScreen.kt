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
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.ui.components.LoadingIndicator
import com.ximena.foodieapp.ui.components.RecipeCard
import com.ximena.foodieapp.ui.screens.favorites.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onNavigateToDetail: (Recipe) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Crear ViewModel
    val viewModel = remember {
        FavoritesViewModel(
            obtenerFavoritas = AppDependencies.provideGetFavoritesUseCase(context),
            guardarFavorita = AppDependencies.provideSaveFavoriteUseCase(context),
            buscarFavoritas = AppDependencies.provideSearchFavoritesUseCase(context)
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
                title = { Text("Mis Favoritas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { mostrarBusqueda = !mostrarBusqueda }) {
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
            // Barra de búsqueda
            if (mostrarBusqueda) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = {
                        textoBusqueda = it
                        viewModel.buscar(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Buscar en favoritas...") },
                    singleLine = true
                )
            }

            // Contenido según el estado
            when (val estadoActual = estado) {
                is FavoritesViewModel.EstadoUi.Cargando -> {
                    LoadingIndicator()
                }

                is FavoritesViewModel.EstadoUi.Exito -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estadoActual.recetas) { receta ->
                            RecipeCard(
                                recipe = receta,
                                onClick = { onNavigateToDetail(receta) },
                                onFavoriteClick = { viewModel.eliminarFavorita(receta) }
                            )
                        }
                    }
                }

                is FavoritesViewModel.EstadoUi.Vacio -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (textoBusqueda.isBlank()) {
                                    "No tienes favoritas guardadas"
                                } else {
                                    "No se encontraron recetas"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )

                            if (textoBusqueda.isNotBlank()) {
                                Button(onClick = {
                                    textoBusqueda = ""
                                    viewModel.cargarFavoritas()
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