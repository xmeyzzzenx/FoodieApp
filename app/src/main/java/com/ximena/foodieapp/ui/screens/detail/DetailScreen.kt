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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ximena.foodieapp.di.AppDependencies
import com.ximena.foodieapp.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    recipeId: Int,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val viewModel = remember(recipeId) {
        DetailViewModel(
            getRecipeById = AppDependencies.provideGetRecipeByIdUseCase(context),
            recipeRepository = AppDependencies.provideRecipeRepository(context),
            toggleFavorite = AppDependencies.provideToggleFavoriteUseCase(context),
            apiKey = AppDependencies.getApiKey(),
            recipeId = recipeId
        )
    }

    val state by viewModel.state.collectAsState()

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
                    val recipe = (state as? DetailViewModel.UiState.Success)?.recipe
                    if (recipe != null) {
                        IconButton(onClick = { viewModel.onToggleFavorite() }) {
                            Icon(
                                imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorita",
                                tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is DetailViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            is DetailViewModel.UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
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
                        Button(onClick = { viewModel.reload() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is DetailViewModel.UiState.Success -> {
                val recipe = s.recipe

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = recipe.imageUrl,
                        contentDescription = recipe.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            InfoLine(label = "Tiempo", value = "${recipe.readyInMinutes} min")
                            InfoLine(label = "Porciones", value = "${recipe.servings}")
                        }

                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = recipe.summary.cleanHtml(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
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

private fun String.cleanHtml(): String = replace(Regex("<[^>]*>"), "")
