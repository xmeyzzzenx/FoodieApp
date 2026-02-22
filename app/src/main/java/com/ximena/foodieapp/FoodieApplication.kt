package com.ximena.foodieapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Punto de entrada de la app para Hilt
// @HiltAndroidApp genera el grafo de dependencias al arrancar
@HiltAndroidApp
class FoodieApplication : Application()