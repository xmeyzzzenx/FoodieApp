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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onNavigateBack: () -> Unit,
    onSave: (titulo: String, minutos: Int, porciones: Int, descripcion: String) -> Unit
) {
    // Estados del formulario
    var titulo by remember { mutableStateOf("") }
    var minutos by remember { mutableStateOf("") }
    var porciones by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Estados de error
    var errorTitulo by remember { mutableStateOf(false) }
    var errorMinutos by remember { mutableStateOf(false) }
    var errorPorciones by remember { mutableStateOf(false) }
    var errorDescripcion by remember { mutableStateOf(false) }

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
            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    errorTitulo = false
                },
                label = { Text("Título de la receta") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorTitulo,
                supportingText = if (errorTitulo) {
                    { Text("El título no puede estar vacío") }
                } else null,
                singleLine = true
            )

            // Minutos de preparación
            OutlinedTextField(
                value = minutos,
                onValueChange = {
                    minutos = it.filter { char -> char.isDigit() }
                    errorMinutos = false
                },
                label = { Text("Minutos de preparación") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMinutos,
                supportingText = if (errorMinutos) {
                    { Text("Ingresa un número válido") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Porciones
            OutlinedTextField(
                value = porciones,
                onValueChange = {
                    porciones = it.filter { char -> char.isDigit() }
                    errorPorciones = false
                },
                label = { Text("Número de porciones") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorPorciones,
                supportingText = if (errorPorciones) {
                    { Text("Ingresa un número válido") }
                } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    errorDescripcion = false
                },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                isError = errorDescripcion,
                supportingText = if (errorDescripcion) {
                    { Text("La descripción no puede estar vacía") }
                } else null,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón guardar
            Button(
                onClick = {
                    // Validar campos
                    errorTitulo = titulo.isBlank()
                    errorMinutos = minutos.isBlank() || minutos.toIntOrNull() == null
                    errorPorciones = porciones.isBlank() || porciones.toIntOrNull() == null
                    errorDescripcion = descripcion.isBlank()

                    // Si todo es válido, guardar
                    if (!errorTitulo && !errorMinutos && !errorPorciones && !errorDescripcion) {
                        onSave(
                            titulo,
                            minutos.toInt(),
                            porciones.toInt(),
                            descripcion
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Receta")
            }
        }
    }
}