package com.ximena.foodieapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ximena.foodieapp.BuildConfig
import com.ximena.foodieapp.data.remote.api.SpoonacularApi
import com.ximena.foodieapp.data.repository.RecipesRepositoryImpl
import com.ximena.foodieapp.domain.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val apiKeyInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("apiKey", BuildConfig.SPOONACULAR_API_KEY)
                .build()

            val newRequest = original.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideSpoonacularApi(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(api: SpoonacularApi): RecipesRepository {
        return RecipesRepositoryImpl(api)
    }
}
