package com.ximena.foodieapp.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState
import com.ximena.foodieapp.ui.components.SearchField

@Composable
fun FavoritesScreen(
    onOpenRecipe: (recipeId: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Favoritas",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SearchField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Buscar favoritas"
            )

            if (favorites.isEmpty()) {
                EmptyState(
                    title = "No tienes favoritas",
                    subtitle = "Marca alguna receta desde Explorar"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(favorites.size) { index ->
                        val item = favorites[index]

                        androidx.compose.material3.Card(
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            androidx.compose.foundation.layout.Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                androidx.compose.foundation.layout.Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = item.title)
                                }

                                IconButton(onClick = { viewModel.remove(item.recipeId) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }

                                androidx.compose.material3.Button(
                                    onClick = { onOpenRecipe(item.recipeId) }
                                ) {
                                    Text(text = "Abrir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
