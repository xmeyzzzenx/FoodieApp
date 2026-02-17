package com.ximena.foodieapp.di

import android.content.Context
import androidx.room.Room
import com.ximena.foodieapp.auth.Auth0Manager
import com.ximena.foodieapp.auth.SecureSessionStore
import com.ximena.foodieapp.data.local.dao.FavoriteDao
import com.ximena.foodieapp.data.local.dao.MealPlanDao
import com.ximena.foodieapp.data.local.dao.UserRecipeDao
import com.ximena.foodieapp.data.local.database.AppDatabase
import com.ximena.foodieapp.data.repository.AuthRepositoryImpl
import com.ximena.foodieapp.data.repository.PlannerRepositoryImpl
import com.ximena.foodieapp.data.repository.UserRecipesRepositoryImpl
import com.ximena.foodieapp.domain.repository.AuthRepository
import com.ximena.foodieapp.domain.repository.PlannerRepository
import com.ximena.foodieapp.domain.repository.UserRecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "foodie_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideMealPlanDao(db: AppDatabase): MealPlanDao = db.mealPlanDao()

    @Provides
    fun provideUserRecipeDao(db: AppDatabase): UserRecipeDao = db.userRecipeDao()

    @Provides
    @Singleton
    fun provideSecureSessionStore(
        @ApplicationContext context: Context
    ): SecureSessionStore {
        return SecureSessionStore(context)
    }

    @Provides
    @Singleton
    fun provideAuth0Manager(
        @ApplicationContext context: Context,
        sessionStore: SecureSessionStore
    ): Auth0Manager {
        return Auth0Manager(context, sessionStore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth0Manager: Auth0Manager
    ): AuthRepository {
        return AuthRepositoryImpl(auth0Manager)
    }

    @Provides
    @Singleton
    fun providePlannerRepository(
        favoriteDao: FavoriteDao,
        mealPlanDao: MealPlanDao
    ): PlannerRepository {
        return PlannerRepositoryImpl(favoriteDao, mealPlanDao)
    }

    @Provides
    @Singleton
    fun provideUserRecipesRepository(
        userRecipeDao: UserRecipeDao
    ): UserRecipesRepository {
        return UserRecipesRepositoryImpl(userRecipeDao)
    }
}
