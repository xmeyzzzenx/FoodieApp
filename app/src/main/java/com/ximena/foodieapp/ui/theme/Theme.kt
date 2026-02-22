package com.ximena.foodieapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    secondary = Color(0xFF795548),
    tertiary = Color(0xFFF57F17),
    primaryContainer = Color(0xFFFFE0B2),
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFF8F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

@Composable
fun FoodieAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
