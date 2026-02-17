package com.ximena.foodieapp.ui.screens.picklocal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ximena.foodieapp.data.local.entity.UserRecipeEntity
import com.ximena.foodieapp.ui.components.AppTopBar
import com.ximena.foodieapp.ui.components.EmptyState
import com.ximena.foodieapp.ui.components.SearchField

@Composable
fun PickLocalScreen(
    dayOfWeek: Int,
    mealType: String,
    onPick: (localId: Long, title: String, image: String?) -> Unit,
    onBack: () -> Unit,
    viewModel: PickLocalViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val recipes by viewModel.recipes.collectAsState()

    val dayLabel = when (dayOfWeek) {
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miércoles"
        4 -> "Jueves"
        5 -> "Viernes"
        6 -> "Sábado"
        7 -> "Domingo"
        else -> "Día $dayOfWeek"
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Elegir receta local",
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
            Text(text = "$dayLabel - $mealType")

            SearchField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Buscar en mis recetas"
            )

            if (recipes.isEmpty()) {
                EmptyState(
                    title = "No hay recetas",
                    subtitle = "Crea una receta para poder añadirla al plan"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(recipes.size) { index ->
                        val item: UserRecipeEntity = recipes[index]

                        androidx.compose.material3.Card(
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = item.title)

                                androidx.compose.material3.Button(
                                    onClick = { onPick(item.localId, item.title, item.imageUrl) },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Text(text = "Elegir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
