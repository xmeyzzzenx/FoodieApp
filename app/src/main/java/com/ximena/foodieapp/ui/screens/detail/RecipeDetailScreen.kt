package com.ximena.foodieapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ximena.foodieapp.domain.model.RecipeDetail
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.ErrorView
import com.ximena.foodieapp.ui.components.LoadingView
import com.ximena.foodieapp.utils.UiState

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    isOnline: Boolean,
    onBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val state by viewModel.detailState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(recipeId, isOnline) {
        viewModel.loadOnline(recipeId)
    }

    val actionIcon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Detalle",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack,
                actionIcon = actionIcon,
                onActionClick = { viewModel.toggleFavorite() }
            )
        }
    ) { padding ->
        when (state) {
            is UiState.Idle -> Unit
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(message = (state as UiState.Error).message)
            is UiState.Success -> {
                val detail = (state as UiState.Success<RecipeDetail>).data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(
                        model = detail.image,
                        contentDescription = null
                    )

                    Text(
                        text = detail.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = buildString {
                            if (detail.readyInMinutes != null) append("Tiempo: ${detail.readyInMinutes} min  ")
                            if (detail.servings != null) append("Raciones: ${detail.servings}")
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Ingredientes",
                        style = MaterialTheme.typography.titleMedium
                    )

                    detail.ingredients.forEach { ing ->
                        Text(
                            text = "• ${ing.original.ifBlank { ing.name }}",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = "Instrucciones",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = detail.instructions.ifBlank { "Sin instrucciones" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
