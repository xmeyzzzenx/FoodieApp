package com.ximena.foodieapp.ui.screens.pickonline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.ximena.foodieapp.ui.components.ErrorView
import com.ximena.foodieapp.ui.components.LoadingView
import com.ximena.foodieapp.ui.components.RecipeCard
import com.ximena.foodieapp.ui.components.SearchField
import com.ximena.foodieapp.utils.UiState

@Composable
fun PickOnlineScreen(
    dayOfWeek: Int,
    mealType: String,
    onPick: (recipeId: Int, title: String, image: String?) -> Unit,
    onBack: () -> Unit,
    viewModel: PickOnlineViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val state by viewModel.state.collectAsState()

    val dayLabel = when (dayOfWeek) {
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miércoles"
        4 -> "Jueves"
        5 -> "Viernes"
        6 -> "Sábado"
        7 -> "Domingo"
        else -> "Día $dayOfWeek"
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Elegir receta",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack
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
            Text(text = "$dayLabel - $mealType")

            SearchField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Buscar receta online"
            )

            Button(onClick = { viewModel.search() }) {
                Text(text = "Buscar")
            }

            when (state) {
                is UiState.Idle -> Unit
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(message = (state as UiState.Error).message)
                is UiState.Success -> {
                    val recipes = (state as UiState.Success<List<Recipe>>).data
                    if (recipes.isEmpty()) {
                        EmptyState(
                            title = "Sin resultados",
                            subtitle = "Prueba otra búsqueda"
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(recipes.size) { index ->
                                val recipe = recipes[index]
                                RecipeCard(
                                    recipe = recipe,
                                    onClick = { onPick(recipe.id, recipe.title, recipe.image) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
