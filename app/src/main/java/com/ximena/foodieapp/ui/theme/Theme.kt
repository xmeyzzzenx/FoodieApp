package com.ximena.foodieapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de colores personalizada basada en naranja (tono comida/cocina)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),            // Naranja oscuro: botones principales
    secondary = Color(0xFF795548),          // Marrón: textos secundarios
    tertiary = Color(0xFFF57F17),           // Amarillo naranja: acentos
    primaryContainer = Color(0xFFFFE0B2),   // Naranja claro: fondos de cards
    background = Color(0xFFFFF8F5),         // Blanco cálido: fondo general
    surface = Color(0xFFFFF8F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),       // Casi negro: texto sobre fondo claro
    onSurface = Color(0xFF1C1B1F),
)

// Tema global de la app: envuelve todo el contenido con los colores definidos
@Composable
fun FoodieAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColorScheme, content = content)
}