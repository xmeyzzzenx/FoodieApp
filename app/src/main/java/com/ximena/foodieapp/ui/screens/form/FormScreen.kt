package com.ximena.foodieapp.ui.screens.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onNavigateBack: () -> Unit,
    onSave: (titulo: String, minutos: Int, porciones: Int, descripcion: String) -> Unit,
    viewModel: FormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Receta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── TÍTULO ──────────────────────────────────────────────────────
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = viewModel::onTituloChange,
                label = { Text("Título de la receta") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorTitulo,
                supportingText = if (uiState.errorTitulo) {
                    { Text("El título no puede estar vacío") }
                } else null,
                singleLine = true
            )

            // ── MINUTOS ─────────────────────────────────────────────────────
            OutlinedTextField(
                value = uiState.minutos,
                onValueChange = viewModel::onMinutosChange,
                label = { Text("Minutos de preparación") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorMinutos,
                supportingText = if (uiState.errorMinutos) {
                    { Text("Ingresa un número válido") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // ── PORCIONES ───────────────────────────────────────────────────
            OutlinedTextField(
                value = uiState.porciones,
                onValueChange = viewModel::onPorcionesChange,
                label = { Text("Número de porciones") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorPorciones,
                supportingText = if (uiState.errorPorciones) {
                    { Text("Ingresa un número válido") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // ── DESCRIPCIÓN ────────────────────────────────────────────────
            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                isError = uiState.errorDescripcion,
                supportingText = if (uiState.errorDescripcion) {
                    { Text("La descripción no puede estar vacía") }
                } else null,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── BOTÓN GUARDAR ───────────────────────────────────────────────
            Button(
                onClick = {
                    val ok = viewModel.validar()
                    if (ok) {
                        onSave(
                            uiState.titulo.trim(),
                            uiState.minutos.toInt(),
                            uiState.porciones.toInt(),
                            uiState.descripcion.trim()
                        )
                        viewModel.limpiar()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Receta")
            }
        }
    }
}
