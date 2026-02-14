package com.ximena.foodieapp.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.ximena.foodieapp.data.local.database.FoodieDatabase
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.usecase.GetFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.GetRecipeByIdUseCase
import com.ximena.foodieapp.domain.usecase.GetRecipesUseCase
import com.ximena.foodieapp.domain.usecase.SaveFavoriteUseCase
import com.ximena.foodieapp.domain.usecase.SearchFavoritesUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Proveedor de dependencias de la aplicación
object AppDependencies {

    private const val API_KEY = "f59b6ebb43e24fc291595844ef8ec38c"
    private const val BASE_URL = "https://api.spoonacular.com/"

    // ═══════════════════════════════════════════════════════════
    // ROOM DATABASE
    // ═══════════════════════════════════════════════════════════

    private var database: FoodieDatabase? = null

    fun provideDatabase(context: Context): FoodieDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                FoodieDatabase::class.java,
                "foodie_database"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { database = it }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // RETROFIT API
    // ═══════════════════════════════════════════════════════════

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun provideSpoonacularApi(): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }

    // ═══════════════════════════════════════════════════════════
    // REPOSITORY
    // ═══════════════════════════════════════════════════════════

    fun provideRecipeRepository(context: Context): RecipeRepository {
        return RecipeRepository(
            api = provideSpoonacularApi(),
            dao = provideDatabase(context).recipeDao()
        )
    }

    // ═══════════════════════════════════════════════════════════
    // USE CASES
    // ═══════════════════════════════════════════════════════════

    fun provideGetRecipesUseCase(context: Context): GetRecipesUseCase {
        return GetRecipesUseCase(
            repository = provideRecipeRepository(context)
        )
    }

    fun provideGetFavoritesUseCase(context: Context): GetFavoritesUseCase {
        return GetFavoritesUseCase(
            repository = provideRecipeRepository(context)
        )
    }

    fun provideSaveFavoriteUseCase(context: Context): SaveFavoriteUseCase {
        return SaveFavoriteUseCase(
            repository = provideRecipeRepository(context)
        )
    }

    fun provideSearchFavoritesUseCase(context: Context): SearchFavoritesUseCase {
        return SearchFavoritesUseCase(
            repository = provideRecipeRepository(context)
        )
    }

    fun provideGetRecipeByIdUseCase(context: Context): GetRecipeByIdUseCase {
        return GetRecipeByIdUseCase(
            repository = provideRecipeRepository(context)
        )
    }

    // ═══════════════════════════════════════════════════════════
    // API KEY
    // ═══════════════════════════════════════════════════════════

    fun getApiKey(): String = API_KEY
}