package com.ximena.foodieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ximena.foodieapp.ui.navigation.FoodieNavHost
import com.ximena.foodieapp.ui.theme.FoodieAppTheme
import dagger.hilt.android.AndroidEntryPoint

// Activity principal: única Activity de la app (arquitectura de una sola Activity)
// @AndroidEntryPoint permite que Hilt inyecte dependencias aquí
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // La UI ocupa toda la pantalla incluyendo barras del sistema
        setContent {
            FoodieAppTheme {
                FoodieNavHost() // Punto de entrada de la navegación
            }
        }
    }
}