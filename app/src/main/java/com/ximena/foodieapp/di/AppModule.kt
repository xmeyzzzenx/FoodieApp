package com.ximena.foodieapp.di

import android.content.Context
import androidx.room.Room
import com.auth0.android.Auth0
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.RecipeDao
import com.ximena.foodieapp.data.local.dao.ShoppingItemDao
import com.ximena.foodieapp.data.local.database.FoodieDatabase
import com.ximena.foodieapp.data.remote.api.MealDbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Módulo de Hilt: aquí se configuran todas las dependencias de la app
// Hilt las inyecta automáticamente donde se necesiten (en repositorios, ViewModels, etc.)
@Module
@InstallIn(SingletonComponent::class) // Estas instancias viven mientras vive la app
object AppModule {

    // Cliente HTTP con logs para ver en Logcat las peticiones y respuestas
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Muestra la URL, headers y body completo
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Instancia de Retrofit apuntando a TheMealDB, usando Gson para parsear el JSON
    @Provides
    @Singleton
    fun provideMealDbApiService(okHttpClient: OkHttpClient): MealDbApiService =
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealDbApiService::class.java)

    // Base de datos Room. fallbackToDestructiveMigration = si sube la versión, borra y recrea
    @Provides
    @Singleton
    fun provideFoodieDatabase(@ApplicationContext context: Context): FoodieDatabase =
        Room.databaseBuilder(context, FoodieDatabase::class.java, "foodie_db")
            .fallbackToDestructiveMigration()
            .build()

    // DAOs obtenidos desde la BD (no son Singleton porque Room los gestiona él solo)
    @Provides
    fun provideRecipeDao(db: FoodieDatabase): RecipeDao = db.recipeDao()

    @Provides
    fun provideMealPlanDao(db: FoodieDatabase): MealPlanDao = db.mealPlanDao()

    @Provides
    fun provideShoppingItemDao(db: FoodieDatabase): ShoppingItemDao = db.shoppingItemDao()

    // Instancia de Auth0 con el clientId y dominio del proyecto
    @Provides
    @Singleton
    fun provideAuth0(): Auth0 =
        Auth0.getInstance(
            "GLlPFFy9Sz9K0pwML1eREsf4gnBtfggf",
            "dev-qjujhqmlbgx8725a.eu.auth0.com"
        )
}