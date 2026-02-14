package com.ximena.foodieapp.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ximena.foodieapp.di.AppDependencies
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.ui.screens.detail.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    receta: Recipe,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    // Crear ViewModel
    val viewModel = remember {
        DetailViewModel(
            guardarFavorita = AppDependencies.provideSaveFavoriteUseCase(context),
            recetaInicial = receta
        )
    }

    // Observar el estado
    val recetaActual by viewModel.receta.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorita() }) {
                        Icon(
                            imageVector = if (recetaActual.esFavorita) {
                                Icons.Default.Favorite
                            } else {
                                Icons.Default.FavoriteBorder
                            },
                            contentDescription = "Favorita",
                            tint = if (recetaActual.esFavorita) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen de la receta
            AsyncImage(
                model = recetaActual.imagen,
                contentDescription = recetaActual.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            // Contenido
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = recetaActual.titulo,
                    style = MaterialTheme.typography.headlineMedium
                )

                // Información básica
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    InfoChip(
                        label = "Tiempo",
                        value = "${recetaActual.minutosPreparacion} min"
                    )

                    InfoChip(
                        label = "Porciones",
                        value = "${recetaActual.porciones}"
                    )
                }

                // Descripción
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = recetaActual.descripcion
                        .replace(Regex("<[^>]*>"), ""),  // Quita HTML tags
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}