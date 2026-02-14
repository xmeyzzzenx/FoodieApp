package com.ximena.foodieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.ximena.foodieapp.ui.navigation.AppNavigation
import com.ximena.foodieapp.ui.theme.FoodieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodieAppTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}