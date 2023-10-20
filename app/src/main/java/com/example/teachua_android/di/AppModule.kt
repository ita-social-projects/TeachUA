package com.example.teachua_android.di

import android.util.Log
import com.example.teachua_android.common.Constants.BASE_URL
import com.example.teachua_android.common.Constants.TAG
import com.example.teachua_android.data.remote.CitiesApi
import com.example.teachua_android.data.repository.CitiesRepositoryImpl
import com.example.teachua_android.domain.repository.CitiesRepository
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
        Log.d(TAG, "Api provided")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CitiesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCitiesRepository(api: CitiesApi): CitiesRepository {
        Log.d(TAG, "Repo provided")
        return CitiesRepositoryImpl(api)
    }
}