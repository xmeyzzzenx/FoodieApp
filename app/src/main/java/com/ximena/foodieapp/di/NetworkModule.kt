package com.ximena.foodieapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ximena.foodieapp.BuildConfig
import com.ximena.foodieapp.data.remote.api.TheMealDbApi
import com.ximena.foodieapp.data.repository.RecipesRepositoryImpl
import com.ximena.foodieapp.domain.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
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
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTheMealDbApi(retrofit: Retrofit): TheMealDbApi {
        return retrofit.create(TheMealDbApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(api: TheMealDbApi): RecipesRepository {
        return RecipesRepositoryImpl(api)
    }
}
