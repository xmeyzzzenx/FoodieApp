package com.ximena.foodieapp.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ximena.foodieapp.data.auth.AuthManager

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit
) {
    LoginScreen(onLoginSuccess = onLoginSuccess)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    val viewModel = remember { AuthViewModel(AuthManager(context)) }
    val state by viewModel.state.collectAsState()

    // Si ya hay sesión, entra directo
    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    // Cuando se loguea, navega
    LaunchedEffect(state) {
        if (state is AuthViewModel.UiState.LoggedIn) {
            onLoginSuccess()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.RestaurantMenu,
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "FoodieApp",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descubre y guarda tus recetas favoritas",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (val s = state) {
                is AuthViewModel.UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthViewModel.UiState.Error -> {
                    Text(
                        text = s.message,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.login(activity) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Entrar con Auth0")
                    }
                }

                else -> {
                    Button(
                        onClick = { viewModel.login(activity) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Entrar con Auth0")
                    }
                }
            }
        }
    }
}
