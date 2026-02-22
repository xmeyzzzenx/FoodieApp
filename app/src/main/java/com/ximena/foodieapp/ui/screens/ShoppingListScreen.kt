package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ximena.foodieapp.ui.viewmodel.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    onBack: () -> Unit,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val items by viewModel.shoppingItems.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    // Separa la lista en pendientes y ya marcados
    val pending = items.filter { !it.isChecked }
    val checked = items.filter { it.isChecked }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de compras 🛒", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } },
                actions = {
                    // Solo muestra el botón de borrar si hay items marcados
                    if (checked.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearChecked() }) {
                            Icon(Icons.Default.Delete, "Limpiar marcados")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Añadir ítem")
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tu lista de compras está vacía")
                    Text("Añade ingredientes desde las recetas", style = MaterialTheme.typography.bodySmall)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Sección de pendientes
                if (pending.isNotEmpty()) {
                    item { Text("Pendiente (${pending.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(vertical = 4.dp)) }
                    items(pending, key = { it.id }) { item ->
                        ShoppingItemRow(name = item.name, measure = item.measure, isChecked = false, recipeName = item.recipeName, onToggle = { viewModel.toggleItem(item.id, true) }, onDelete = { viewModel.deleteItem(item) })
                    }
                }
                // Sección de completados (aparecen tachados)
                if (checked.isNotEmpty()) {
                    item { Spacer(modifier = Modifier.height(8.dp)); Text("Completado (${checked.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(vertical = 4.dp)) }
                    items(checked, key = { it.id }) { item ->
                        ShoppingItemRow(name = item.name, measure = item.measure, isChecked = true, recipeName = item.recipeName, onToggle = { viewModel.toggleItem(item.id, false) }, onDelete = { viewModel.deleteItem(item) })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddShoppingItemDialog(onDismiss = { showAddDialog = false }, onAdd = { name, measure -> viewModel.addItem(name, measure); showAddDialog = false })
    }
}

// Fila de un item: checkbox, nombre, cantidad y botón de borrar
@Composable
fun ShoppingItemRow(name: String, measure: String, isChecked: Boolean, recipeName: String?, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // Fondo más apagado si ya está marcado
        colors = CardDefaults.cardColors(containerColor = if (isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$name${if (measure.isNotBlank()) " - $measure" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    // Tachado si está marcado como comprado
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                )
                // Nombre de la receta de donde viene (si lo tiene)
                recipeName?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary) }
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Close, "Eliminar", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }
    }
}

// Dialog para añadir un item a mano (nombre obligatorio, cantidad opcional)
@Composable
fun AddShoppingItemDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var measure by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir ítem") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("Ingrediente *") },
                    isError = nameError,
                    supportingText = { if (nameError) Text("El nombre es obligatorio", color = MaterialTheme.colorScheme.error) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(value = measure, onValueChange = { measure = it }, label = { Text("Cantidad (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isBlank()) { nameError = true; return@Button }
                onAdd(name, measure)
            }) { Text("Añadir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}