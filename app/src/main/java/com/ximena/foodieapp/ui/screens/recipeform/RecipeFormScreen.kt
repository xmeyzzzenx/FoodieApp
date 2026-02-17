package com.ximena.foodieapp.ui.screens.recipeform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.LoadingView

@Composable
fun RecipeFormScreen(
    localId: Long,
    onSaved: () -> Unit,
    onBack: () -> Unit,
    viewModel: RecipeFormViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(localId) {
        viewModel.load(localId)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (localId > 0L) "Editar receta" else "Nueva receta",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = onBack
            )
        }
    ) { padding ->
        if (state.isSaving) {
            LoadingView()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text(text = "Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.imageUrl,
                    onValueChange = viewModel::onImageUrlChange,
                    label = { Text(text = "URL imagen (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.readyInMinutes,
                    onValueChange = viewModel::onReadyChange,
                    label = { Text(text = "Tiempo (min)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.servings,
                    onValueChange = viewModel::onServingsChange,
                    label = { Text(text = "Raciones") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.ingredientsText,
                    onValueChange = viewModel::onIngredientsChange,
                    label = { Text(text = "Ingredientes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                OutlinedTextField(
                    value = state.instructionsText,
                    onValueChange = viewModel::onInstructionsChange,
                    label = { Text(text = "Instrucciones") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                if (!state.error.isNullOrBlank()) {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = { viewModel.save(onSaved) },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Guardar")
                }
            }
        }
    }
}
