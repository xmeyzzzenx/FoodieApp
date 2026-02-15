package com.ximena.foodieapp.di

import android.content.Context
import androidx.room.Room
import com.ximena.foodieapp.BuildConfig
import com.ximena.foodieapp.data.local.database.FoodieDatabase
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.data.repository.MealPlanRepository
import com.ximena.foodieapp.data.repository.RecipeRepository
import com.ximena.foodieapp.domain.usecase.mealplan.AddMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.mealplan.DeleteMealPlanUseCase
import com.ximena.foodieapp.domain.usecase.mealplan.GetMealPlansUseCase
import com.ximena.foodieapp.domain.usecase.recipes.GetFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.GetRecipeByIdUseCase
import com.ximena.foodieapp.domain.usecase.recipes.GetRecipesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.SearchFavoritesUseCase
import com.ximena.foodieapp.domain.usecase.recipes.ToggleFavoriteUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Proveedor de dependencias (DI manual)
object AppDependencies {

    private const val BASE_URL = "https://api.spoonacular.com/"

    // Instancia única de Room
    private var database: FoodieDatabase? = null

    fun provideDatabase(context: Context): FoodieDatabase {
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                FoodieDatabase::class.java,
                "foodie_database"
            )
                .fallbackToDestructiveMigration() // Reinicia BD si cambia
                .build()
                .also { database = it }
        }
    }

    // OkHttp
    private fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY // Logs en debug
            } else {
                HttpLoggingInterceptor.Level.NONE // Sin logs en release
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit
    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API
    fun provideSpoonacularApi(): SpoonacularApi {
        return provideRetrofit().create(SpoonacularApi::class.java)
    }

    // Repositories
    fun provideRecipeRepository(context: Context): RecipeRepository {
        return RecipeRepository(
            api = provideSpoonacularApi(),
            dao = provideDatabase(context).recipeDao()
        )
    }

    fun provideMealPlanRepository(context: Context): MealPlanRepository {
        return MealPlanRepository(
            dao = provideDatabase(context).mealPlanDao()
        )
    }

    // UseCases (Recipes)
    fun provideGetRecipesUseCase(context: Context) =
        GetRecipesUseCase(provideRecipeRepository(context))

    fun provideGetFavoritesUseCase(context: Context) =
        GetFavoritesUseCase(provideRecipeRepository(context))

    fun provideSearchFavoritesUseCase(context: Context) =
        SearchFavoritesUseCase(provideRecipeRepository(context))

    fun provideGetRecipeByIdUseCase(context: Context) =
        GetRecipeByIdUseCase(provideRecipeRepository(context))

    fun provideToggleFavoriteUseCase(context: Context) =
        ToggleFavoriteUseCase(provideRecipeRepository(context))

    // UseCases (MealPlan)
    fun provideGetMealPlansUseCase(context: Context) =
        GetMealPlansUseCase(provideMealPlanRepository(context))

    fun provideAddMealPlanUseCase(context: Context) =
        AddMealPlanUseCase(provideMealPlanRepository(context))

    fun provideDeleteMealPlanUseCase(context: Context) =
        DeleteMealPlanUseCase(provideMealPlanRepository(context))

    // API key (BuildConfig)
    fun getApiKey(): String = BuildConfig.SPOONACULAR_API_KEY
}
