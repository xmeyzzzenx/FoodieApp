package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.domain.model.Recipe

// Pantalla de detalle de una receta
@Composable
fun DetailScreen(
    recipe: Recipe?,
    onFavoriteClick: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        recipe?.let {
            Text(
                text = it.titulo,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tiempo: ${it.minutosPreparacion} minutos")
            Text("Porciones: ${it.porciones}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(it.descripcion)
        }
    }
}