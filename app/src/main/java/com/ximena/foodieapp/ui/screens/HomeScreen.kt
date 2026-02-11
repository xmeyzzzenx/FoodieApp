package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.domain.model.Recipe

// Pantalla principal con lista de recetas
@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteClick: (Recipe) -> Unit,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        // Barra de búsqueda
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(it)
            },
            label = { Text("Buscar recetas...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Lista de recetas
        LazyColumn {
            items(recipes) { recipe ->
                Text(
                    text = recipe.titulo,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}