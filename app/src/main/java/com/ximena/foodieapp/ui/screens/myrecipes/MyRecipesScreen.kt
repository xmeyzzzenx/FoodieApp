package com.ximena.foodieapp.ui.screens.myrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState
import com.ximena.foodieapp.ui.components.RecipeCard
import com.ximena.foodieapp.ui.components.SearchField

@Composable
fun MyRecipesScreen(
    onAddNew: () -> Unit,
    onEdit: (localId: Long) -> Unit,
    onBack: () -> Unit,
    viewModel: MyRecipesViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val recipes by viewModel.recipes.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Mis recetas",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack,
                actionIcon = Icons.Default.Add,
                onActionClick = onAddNew
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SearchField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Buscar mis recetas"
            )

            if (recipes.isEmpty()) {
                EmptyState(
                    title = "No tienes recetas",
                    subtitle = "Pulsa + para crear una"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recipes.size) { index ->
                        val item = recipes[index]

                        RecipeCard(
                            recipe = Recipe(
                                id = item.localId.toInt(),
                                title = item.title,
                                image = item.imageUrl,
                            ),
                            onClick = { onEdit(item.localId) }
                        )

                        Row(
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            IconButton(onClick = { onEdit(item.localId) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { viewModel.delete(item.localId) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar")
                            }
                        }
                    }
                }
            }
        }
    }
}
