package com.ximena.foodieapp.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    AsyncImage(
                        model = detail.image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                    )

                    Text(
                        text = detail.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    SectionCard(
                        title = "Ingredientes",
                        emptyText = "Sin ingredientes"
                    ) {
                        if (detail.ingredients.isEmpty()) {
                            Text(
                                text = "Sin ingredientes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                            )
                        } else {
                            detail.ingredients.forEach { ing ->
                                val line = ing.original.ifBlank { ing.name }.trim()
                                if (line.isNotBlank()) {
                                    Text(
                                        text = "• $line",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

                    SectionCard(
                        title = "Instrucciones",
                        emptyText = "Sin instrucciones"
                    ) {
                        val formatted = formatInstructions(detail.instructions)
                        Text(
                            text = formatted.ifBlank { "Sin instrucciones" },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    emptyText: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            content()
        }
    }
}

private fun formatInstructions(raw: String): String {
    val cleaned = raw
        .replace("\r\n", "\n")
        .replace("\r", "\n")
        .trim()

    // Si viene todo pegado sin saltos, al menos lo dejamos con espacios limpios
    return cleaned
        .replace(Regex("[ \t]+"), " ")
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()
}
