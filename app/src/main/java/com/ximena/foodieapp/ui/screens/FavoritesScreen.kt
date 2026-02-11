package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.domain.model.Recipe

// Pantalla de recetas favoritas guardadas localmente
@Composable
fun FavoritesScreen(
    favorites: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Mis recetas favoritas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes recetas favoritas")
            }
        } else {
            LazyColumn {
                items(favorites) { recipe ->
                    Text(
                        text = recipe.titulo,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}