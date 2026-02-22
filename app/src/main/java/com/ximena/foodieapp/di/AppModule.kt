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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMealDbApiService(okHttpClient: OkHttpClient): MealDbApiService =
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealDbApiService::class.java)

    @Provides
    @Singleton
    fun provideFoodieDatabase(@ApplicationContext context: Context): FoodieDatabase =
        Room.databaseBuilder(context, FoodieDatabase::class.java, "foodie_db").build()

    @Provides
    fun provideRecipeDao(db: FoodieDatabase): RecipeDao = db.recipeDao()

    @Provides
    fun provideMealPlanDao(db: FoodieDatabase): MealPlanDao = db.mealPlanDao()

    @Provides
    fun provideShoppingItemDao(db: FoodieDatabase): ShoppingItemDao = db.shoppingItemDao()

    @Provides
    @Singleton
    fun provideAuth0(): Auth0 =
        Auth0.getInstance(
            "GLlPFFy9Sz9K0pwML1eREsf4gnBtfggf",
            "dev-qjujhqmlbgx8725a.eu.auth0.com"
        )
}
