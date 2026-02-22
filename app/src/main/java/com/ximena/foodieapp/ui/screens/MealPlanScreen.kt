package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ximena.foodieapp.domain.model.DayOfWeek
import com.ximena.foodieapp.domain.model.MealPlan
import com.ximena.foodieapp.ui.viewmodel.MealPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    onBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: MealPlanViewModel = hiltViewModel()
) {
    val mealPlans by viewModel.mealPlans.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan semanal 📅", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToSearch,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Añadir receta") }
            )
        }
    ) { padding ->
        if (mealPlans.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📅", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tu plan semanal está vacío")
                    Text("Busca una receta y agrégala al plan", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            // Agrupa las entradas por día para mostrar una card por día
            val grouped = mealPlans.groupBy { it.dayOfWeek }
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Recorre los días en orden (lunes → domingo) y solo muestra los que tienen algo
                DayOfWeek.entries.forEach { day ->
                    val dayMeals = grouped[day]
                    if (!dayMeals.isNullOrEmpty()) {
                        item(key = day.name) {
                            DaySection(day = day, meals = dayMeals, onDelete = { viewModel.removeMealPlan(it) })
                        }
                    }
                }
            }
        }
    }
}

// Card de un día con todas sus comidas listadas
@Composable
fun DaySection(day: DayOfWeek, meals: List<MealPlan>, onDelete: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = day.displayName(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Ordenado por tipo de comida: desayuno → comida → cena
            meals.sortedBy { it.mealType.ordinal }.forEach { meal ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meal.mealType.displayName(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.width(70.dp)
                    )
                    AsyncImage(model = meal.recipeThumbnail, contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = meal.recipeName, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onDelete(meal.id) }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
                if (meals.last() != meal) HorizontalDivider()
            }
        }
    }
}