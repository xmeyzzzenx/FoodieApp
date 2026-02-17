package com.ximena.foodieapp.ui.screens.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState

@Composable
fun MealPlannerScreen(
    onPickOnline: (day: String, mealType: String) -> Unit,
    onPickLocal: (day: String, mealType: String) -> Unit,
    onBack: () -> Unit,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val plan by viewModel.plan.collectAsState()

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (plan.isEmpty()) {
                EmptyState(
                    title = "No hay plan todavía",
                    subtitle = "Añade recetas a tu semana"
                )
            }

            days.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )

                meals.forEach { mealType ->
                    val slot = plan.firstOrNull { it.dayOfWeek == day && it.mealType == mealType }

                    Card(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = mealType, style = MaterialTheme.typography.bodyLarge)
                                Text(text = slot?.title ?: "Vacío", style = MaterialTheme.typography.bodyMedium)
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(onClick = { onPickOnline(day, mealType) }) {
                                    Text(text = "Online")
                                }
                                Button(onClick = { onPickLocal(day, mealType) }) {
                                    Text(text = "Local")
                                }
                                if (slot != null) {
                                    Button(onClick = { viewModel.clearSlot(day, mealType) }) {
                                        Text(text = "Borrar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
