package com.example.teachua_android.di

import android.util.Log
import com.example.teachua_android.common.Constants.BASE_URL
import com.example.teachua_android.common.Constants.TAG
import com.example.teachua_android.data.remote.CategoryApi
import com.example.teachua_android.data.remote.CitiesApi
import com.example.teachua_android.data.remote.ClubsApi
import com.example.teachua_android.data.repository.CategoryRepositoryImpl
import com.example.teachua_android.data.repository.CitiesRepositoryImpl
import com.example.teachua_android.data.repository.ClubsRepositoryImpl
import com.example.teachua_android.domain.repository.CategoryRepository
import com.example.teachua_android.domain.repository.CitiesRepository
import com.example.teachua_android.domain.repository.ClubsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCitiesApi(): CitiesApi {
        Log.d(TAG, "Cities Api provided")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CitiesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClubsApi(): ClubsApi {
        Log.d(TAG, "Clubs Api provided")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClubsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(): CategoryApi {
        Log.d(TAG, "Category Api provided")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(api: CategoryApi): CategoryRepository {
        Log.d(TAG, "Category repo provided")
        return CategoryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCitiesRepository(api: CitiesApi): CitiesRepository {
        Log.d(TAG, "Cities repo provided")
        return CitiesRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideClubsRepository(api: ClubsApi): ClubsRepository {
        Log.d(TAG, "Clubs repo provided")
        return ClubsRepositoryImpl(api)
    }
}