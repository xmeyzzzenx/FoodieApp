package com.ximena.foodieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ximena.foodieapp.ui.components.RecipeDetailCard
import com.ximena.foodieapp.ui.viewmodel.MyRecipesViewModel
import com.ximena.foodieapp.ui.viewmodel.RecipeFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipesScreen(
    onRecipeClick: (String) -> Unit,
    onAddRecipe: () -> Unit,
    onEditRecipe: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: MyRecipesViewModel = hiltViewModel()
) {
    val myRecipes by viewModel.myRecipes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis recetas 👨‍🍳", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRecipe) {
                Icon(Icons.Default.Add, "Nueva receta")
            }
        }
    ) { padding ->
        if (myRecipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("👨‍🍳", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No tienes recetas propias aún")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onAddRecipe) {
                        Icon(Icons.Default.Add, null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Crear primera receta")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myRecipes, key = { it.id }) { recipe ->
                    RecipeDetailCard(recipe = recipe, onClick = { onRecipeClick(recipe.id) })
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onEditRecipe(recipe.id) }) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar")
                        }
                        TextButton(onClick = { viewModel.deleteRecipe(recipe) }) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Eliminar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFormScreen(
    recipeId: String?,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: RecipeFormViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(recipeId) { recipeId?.let { viewModel.loadRecipeForEdit(it) } }
    LaunchedEffect(formState.savedSuccessfully) { if (formState.savedSuccessfully) onSaved() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (recipeId == null) "Nueva receta" else "Editar receta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                },
                actions = {
                    if (formState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 16.dp))
                    } else {
                        TextButton(onClick = { viewModel.saveRecipe() }) {
                            Text("Guardar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre *") },
                isError = formState.nameError != null,
                supportingText = { formState.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.category,
                onValueChange = { viewModel.onCategoryChange(it) },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.area,
                onValueChange = { viewModel.onAreaChange(it) },
                label = { Text("Origen/Cocina") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.thumbnailUrl,
                onValueChange = { viewModel.onThumbnailUrlChange(it) },
                label = { Text("URL de imagen (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.ingredientsText,
                onValueChange = { viewModel.onIngredientsChange(it) },
                label = { Text("Ingredientes (separados por coma)") },
                placeholder = { Text("harina, huevos, leche") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            OutlinedTextField(
                value = formState.measuresText,
                onValueChange = { viewModel.onMeasuresChange(it) },
                label = { Text("Cantidades (separadas por coma)") },
                placeholder = { Text("200g, 2, 250ml") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            OutlinedTextField(
                value = formState.instructions,
                onValueChange = { viewModel.onInstructionsChange(it) },
                label = { Text("Instrucciones *") },
                isError = formState.instructionsError != null,
                supportingText = { formState.instructionsError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )
            Button(
                onClick = { viewModel.saveRecipe() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !formState.isSaving
            ) {
                Text(if (recipeId == null) "Crear receta" else "Guardar cambios")
            }
        }
    }
}
