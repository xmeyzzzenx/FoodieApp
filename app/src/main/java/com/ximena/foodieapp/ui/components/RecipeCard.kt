package com.ximena.foodieapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.domain.model.Recipe

// Tarjeta reutilizable para mostrar una receta en la lista
@Composable
fun RecipeCard(
    recipe: Recipe,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteClick: (Recipe) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onRecipeClick(recipe) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Título de la receta
            Text(
                text = recipe.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tiempo y porciones
            Text("Tiempo: ${recipe.minutosPreparacion} minutos")
            Text("Porciones: ${recipe.porciones}")

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de favorita
            Button(onClick = { onFavoriteClick(recipe) }) {
                Text(if (recipe.esFavorita) "Quitar de favoritas" else "Añadir a favoritas")
            }
        }
    }
}