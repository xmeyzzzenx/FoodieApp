package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Pantalla de formulario para crear una receta propia
@Composable
fun FormScreen(
    onSave: (titulo: String, minutosPreparacion: Int, porciones: Int, descripcion: String) -> Unit,
    onBack: () -> Unit
) {
    // Estado de cada campo del formulario
    var titulo by remember { mutableStateOf("") }
    var minutos by remember { mutableStateOf("") }
    var porciones by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // Estado de errores de validación
    var errorTitulo by remember { mutableStateOf(false) }
    var errorMinutos by remember { mutableStateOf(false) }
    var errorPorciones by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Botón volver
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crear receta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo título
        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                errorTitulo = false // limpia el error al escribir
            },
            label = { Text("Título de la receta") },
            isError = errorTitulo, // muestra el campo en rojo si hay error
            supportingText = {
                if (errorTitulo) Text("El título no puede estar vacío")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo minutos de preparación
        OutlinedTextField(
            value = minutos,
            onValueChange = {
                minutos = it
                errorMinutos = false
            },
            label = { Text("Minutos de preparación") },
            isError = errorMinutos,
            supportingText = {
                if (errorMinutos) Text("Introduce un número válido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo porciones
        OutlinedTextField(
            value = porciones,
            onValueChange = {
                porciones = it
                errorPorciones = false
            },
            label = { Text("Número de porciones") },
            isError = errorPorciones,
            supportingText = {
                if (errorPorciones) Text("Introduce un número válido")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo descripción
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            minLines = 3, // campo más alto para texto largo
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón guardar con validación
        Button(
            onClick = {
                // Validamos cada campo antes de guardar
                errorTitulo = titulo.isBlank()
                errorMinutos = minutos.toIntOrNull() == null
                errorPorciones = porciones.toIntOrNull() == null

                // Solo guardamos si no hay errores
                if (!errorTitulo && !errorMinutos && !errorPorciones) {
                    onSave(titulo, minutos.toInt(), porciones.toInt(), descripcion)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar receta")
        }
    }
}