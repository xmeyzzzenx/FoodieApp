package com.ximena.foodieapp.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.ui.components.*
import com.ximena.foodieapp.ui.viewmodel.AuthViewModel
import com.ximena.foodieapp.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRecipeClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToMealPlan: () -> Unit,
    onNavigateToMyRecipes: () -> Unit,
    onNavigateToShopping: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val categoriesState by viewModel.categoriesState.collectAsStateWithLifecycle()
    val mealsState by viewModel.mealsState.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val randomMealState by viewModel.randomMealState.collectAsStateWithLifecycle()
    val userInfo by authViewModel.userInfo.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FoodieApp 🍽️", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, "Buscar")
                    }
                    IconButton(onClick = {
                        val activity = context as? Activity ?: return@IconButton
                        authViewModel.logout(activity)
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Saludo con nombre del usuario de Auth0
            item {
                Text(
                    text = "¡Hola, ${userInfo?.name ?: "Chef"}! 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (!userInfo?.email.isNullOrBlank()) {
                    Text(
                        text = userInfo!!.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Botones rápidos a las secciones principales
            item {
                SectionTitle("Acceso rápido")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAccessCard(icon = Icons.Default.Favorite, label = "Favoritas", onClick = onNavigateToFavorites, modifier = Modifier.weight(1f))
                    QuickAccessCard(icon = Icons.Default.DateRange, label = "Plan Semanal", onClick = onNavigateToMealPlan, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAccessCard(icon = Icons.Default.ShoppingCart, label = "Compras", onClick = onNavigateToShopping, modifier = Modifier.weight(1f))
                    QuickAccessCard(icon = Icons.Default.Person, label = "Mis Recetas", onClick = onNavigateToMyRecipes, modifier = Modifier.weight(1f))
                }
            }

            // Receta aleatoria del día con botón para cambiarla
            item {
                SectionTitle("Receta del día 🎲")
                when (val state = randomMealState) {
                    is UiState.Success -> {
                        val meal = state.data
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(model = meal.thumbnailUrl, contentDescription = meal.name, modifier = Modifier.size(80.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(meal.name, fontWeight = FontWeight.Bold)
                                    Text(meal.category, style = MaterialTheme.typography.bodySmall)
                                    TextButton(onClick = { onRecipeClick(meal.id) }) { Text("Ver receta →") }
                                }
                                // Refresca la receta del día
                                IconButton(onClick = { viewModel.refreshRandom() }) {
                                    Icon(Icons.Default.Refresh, "Nueva sugerencia")
                                }
                            }
                        }
                    }
                    is UiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    else -> {}
                }
            }

            // Chips de categorías: al pulsar filtra las recetas de abajo
            item {
                SectionTitle("Categorías")
                when (val state = categoriesState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
                    is UiState.Success -> {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.data) { category ->
                                FilterChip(
                                    selected = selectedCategory == category.name,
                                    onClick = { viewModel.selectCategory(category.name) },
                                    label = { Text(category.name) }
                                )
                            }
                        }
                    }
                }
            }

            // Recetas de la categoría seleccionada en grid de 2 columnas
            // chunked(2) parte la lista en grupos de 2 para hacer las filas a mano
            item {
                when (val state = mealsState) {
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Error -> ErrorScreen(state.message)
                    is UiState.Success -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            state.data.chunked(2).forEach { rowMeals ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                    rowMeals.forEach { meal ->
                                        RecipeCard(meal = meal, onClick = { onRecipeClick(meal.id) }, modifier = Modifier.weight(1f))
                                    }
                                    // Si la fila tiene solo 1 receta, rellena el espacio vacío
                                    if (rowMeals.size == 1) Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}