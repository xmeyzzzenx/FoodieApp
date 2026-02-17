package com.ximena.foodieapp.ui.screens.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState

@Composable
fun MealPlannerScreen(
    onPickOnline: (dayOfWeek: Int, mealType: String) -> Unit,
    onPickLocal: (dayOfWeek: Int, mealType: String) -> Unit,
    onBack: () -> Unit,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val plan by viewModel.plan.collectAsState()
    val weekKey by viewModel.weekKey.collectAsState()

    val days = listOf(
        1 to "Lunes",
        2 to "Martes",
        3 to "Miércoles",
        4 to "Jueves",
        5 to "Viernes",
        6 to "Sábado",
        7 to "Domingo"
    )
    val meals = listOf("Breakfast", "Lunch", "Dinner")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Plan semanal",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Semana", style = MaterialTheme.typography.titleMedium)
                            Text(text = weekKey, style = MaterialTheme.typography.bodyMedium)
                        }

                        IconButton(onClick = { viewModel.prevWeek() }) {
                            Text("‹", style = MaterialTheme.typography.headlineMedium)
                        }
                        IconButton(onClick = { viewModel.nextWeek() }) {
                            Text("›", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }
            }

            if (plan.isEmpty()) {
                item {
                    EmptyState(
                        title = "No hay plan para esta semana",
                        subtitle = "Añade recetas a tu semana"
                    )
                }
            }

            days.forEach { (dayNumber, dayLabel) ->
                item {
                    Text(
                        text = dayLabel,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            meals.forEach { mealType ->
                                val slot = plan.firstOrNull {
                                    it.dayOfWeek == dayNumber && it.mealType == mealType
                                }

                                MealSlotRow(
                                    mealType = mealType,
                                    title = slot?.title,
                                    imageUrl = slot?.image,
                                    onPickOnline = { onPickOnline(dayNumber, mealType) },
                                    onPickLocal = { onPickLocal(dayNumber, mealType) },
                                    onDelete = if (slot != null) {
                                        { viewModel.clearSlot(dayNumber, mealType) }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun MealSlotRow(
    mealType: String,
    title: String?,
    imageUrl: String?,
    onPickOnline: () -> Unit,
    onPickLocal: () -> Unit,
    onDelete: (() -> Unit)?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(10.dp))
        } else {
            Spacer(modifier = Modifier.width(54.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = mealType, style = MaterialTheme.typography.titleMedium)
            Text(
                text = title ?: "Vacío",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = false,
                onClick = onPickOnline,
                label = { Text("Online") }
            )
            FilterChip(
                selected = false,
                onClick = onPickLocal,
                label = { Text("Local") }
            )

            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Borrar")
                }
            }
        }
    }
}
