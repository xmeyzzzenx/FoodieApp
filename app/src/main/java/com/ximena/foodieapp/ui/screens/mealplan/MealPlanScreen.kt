package com.ximena.foodieapp.ui.screens.mealplan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.di.AppDependencies
import com.ximena.foodieapp.domain.model.MealPlan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val viewModel = remember {
        MealPlanViewModel(
            getMealPlans = AppDependencies.provideGetMealPlansUseCase(context),
            addMealPlan = AppDependencies.provideAddMealPlanUseCase(context),
            deleteMealPlan = AppDependencies.provideDeleteMealPlanUseCase(context)
        )
    }

    val plans by viewModel.plans.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("Monday") }
    var selectedMealType by remember { mutableStateOf("Breakfast") }
    var recipeTitle by remember { mutableStateOf("") }
    var errorTitle by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan semanal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // abrir diálogo “rápido”
                        selectedDay = "Monday"
                        selectedMealType = "Breakfast"
                        recipeTitle = ""
                        errorTitle = false
                        showDialog = true
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(viewModel.daysOfWeek) { day ->
                DayCard(
                    day = day,
                    plans = plans.filter { it.dayOfWeek == day },
                    mealTypes = viewModel.mealTypes,
                    onAdd = { mealType ->
                        selectedDay = day
                        selectedMealType = mealType
                        recipeTitle = ""
                        errorTitle = false
                        showDialog = true
                    },
                    onDelete = { plan ->
                        viewModel.delete(plan)
                    }
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Añadir comida") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Día: $selectedDay")
                    Text("Tipo: $selectedMealType")

                    OutlinedTextField(
                        value = recipeTitle,
                        onValueChange = {
                            recipeTitle = it
                            errorTitle = false
                        },
                        label = { Text("Nombre de la receta") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorTitle,
                        supportingText = if (errorTitle) {
                            { Text("No puede estar vacío") }
                        } else null,
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        errorTitle = recipeTitle.isBlank()
                        if (!errorTitle) {
                            viewModel.add(
                                dayOfWeek = selectedDay,
                                mealType = selectedMealType,
                                recipeId = 0,
                                recipeTitle = recipeTitle.trim()
                            )
                            showDialog = false
                        }
                    }
                ) {
                    Text("Añadir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun DayCard(
    day: String,
    plans: List<MealPlan>,
    mealTypes: List<String>,
    onAdd: (String) -> Unit,
    onDelete: (MealPlan) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = day,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            mealTypes.forEachIndexed { index, type ->
                val plan = plans.find { it.mealType == type }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (plan != null) {
                            Text(
                                text = plan.recipeTitle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            Text(
                                text = "(vacío)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.clickable { onAdd(type) }
                            )
                        }
                    }

                    if (plan != null) {
                        IconButton(onClick = { onDelete(plan) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                if (index != mealTypes.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}
