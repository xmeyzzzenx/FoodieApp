package com.ximena.foodieapp.ui.screens.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ximena.foodieapp.domain.model.Recipe
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState
import com.ximena.foodieapp.ui.components.ErrorView
import com.ximena.foodieapp.ui.components.LoadingView
import com.ximena.foodieapp.ui.components.RecipeCard
import com.ximena.foodieapp.ui.components.SearchField
import com.ximena.foodieapp.utils.UiState

@Composable
fun ExploreScreen(
    onOpenRecipe: (recipeId: Int, isOnline: Boolean) -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenPlanner: () -> Unit,
    onOpenMyRecipes: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    val query by viewModel.query.collectAsState()
    val state by viewModel.recipesState.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Explorar",
                navigationIcon = Icons.Default.Person,
                onNavigationClick = onOpenMyRecipes,
                actionIcon = Icons.Default.ExitToApp,
                onActionClick = {
                    if (activity != null) {
                        viewModel.logout(activity) { onLogout() }
                    }
                }
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
            if (userName.isNotBlank()) {
                Text(
                    text = "$userName  $userEmail",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            SearchField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Buscar recetas"
            )

            Button(onClick = { viewModel.search() }) {
                Text(text = "Buscar")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onOpenFavorites) {
                    androidx.compose.material3.Icon(Icons.Default.Favorite, contentDescription = null)
                    Text(text = "Favoritas", modifier = Modifier.padding(start = 8.dp))
                }
                OutlinedButton(onClick = onOpenPlanner) {
                    androidx.compose.material3.Icon(Icons.Default.List, contentDescription = null)
                    Text(text = "Plan semanal", modifier = Modifier.padding(start = 8.dp))
                }
            }

            when (state) {
                is UiState.Idle -> Unit
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(message = (state as UiState.Error).message) {
                    viewModel.loadRecipes(query.ifBlank { null })
                }
                is UiState.Success -> {
                    val recipes = (state as UiState.Success<List<Recipe>>).data
                    if (recipes.isEmpty()) {
                        EmptyState(
                            title = "Sin resultados",
                            subtitle = "Prueba otra búsqueda"
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(recipes.size) { index ->
                                val recipe = recipes[index]
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = { onOpenRecipe(recipe.id, true) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
