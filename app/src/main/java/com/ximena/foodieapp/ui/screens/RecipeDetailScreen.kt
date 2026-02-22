package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ximena.foodieapp.domain.model.DayOfWeek
import com.ximena.foodieapp.domain.model.MealType
import com.ximena.foodieapp.domain.model.UiState
import com.ximena.foodieapp.ui.components.ErrorScreen
import com.ximena.foodieapp.ui.components.LoadingScreen
import com.ximena.foodieapp.ui.components.SectionTitle
import com.ximena.foodieapp.ui.viewmodel.RecipeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    onAddToMealPlan: () -> Unit,
    onAddToShopping: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val recipeState by viewModel.recipeState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMealPlanDialog by remember { mutableStateOf(false) }

    LaunchedEffect(mealId) { viewModel.loadRecipe(mealId) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle de receta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = recipeState) {
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> ErrorScreen(state.message, onRetry = { viewModel.loadRecipe(mealId) })
            is UiState.Success -> {
                val recipe = state.data
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    item {
                        AsyncImage(
                            model = recipe.thumbnailUrl,
                            contentDescription = recipe.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(250.dp)
                        )
                    }
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(recipe.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                    Text(
                                        "${recipe.category} · ${recipe.area}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleFavorite() }) {
                                    Icon(
                                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = if (recipe.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            if (recipe.tags.isNotEmpty()) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    items(recipe.tags) { tag ->
                                        AssistChip(onClick = {}, label = { Text(tag, style = MaterialTheme.typography.labelSmall) })
                                    }
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.addToShoppingList(recipe); onAddToShopping() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Compras")
                                }
                                Button(
                                    onClick = { showMealPlanDialog = true },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.DateRange, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Planificar")
                                }
                            }

                            SectionTitle("Ingredientes (${recipe.ingredients.size})")
                            recipe.getIngredientsWithMeasures().forEach { (ingredient, measure) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("• $ingredient", style = MaterialTheme.typography.bodyMedium)
                                    Text(measure, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                                HorizontalDivider(thickness = 0.5.dp)
                            }

                            SectionTitle("Instrucciones")
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = recipe.instructions,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }

                if (showMealPlanDialog) {
                    MealPlanDialog(
                        onDismiss = { showMealPlanDialog = false },
                        onConfirm = { day, mealType ->
                            viewModel.addToMealPlan(recipe, day, mealType)
                            showMealPlanDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MealPlanDialog(
    onDismiss: () -> Unit,
    onConfirm: (DayOfWeek, MealType) -> Unit
) {
    var selectedDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir al plan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Día:", fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(DayOfWeek.entries) { day ->
                        FilterChip(
                            selected = selectedDay == day,
                            onClick = { selectedDay = day },
                            label = { Text(day.displayName().take(3)) }
                        )
                    }
                }
                Text("Comida:", fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    MealType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedMealType == type,
                            onClick = { selectedMealType = type },
                            label = { Text(type.displayName()) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedDay, selectedMealType) }) { Text("Añadir") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
