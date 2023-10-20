package com.example.teachua_android.data.remote

import com.example.teachua_android.domain.model.city.City
import retrofit2.http.GET
import retrofit2.http.Path

interface CitiesApi {
    @GET("api/cities")
    suspend fun getCities(): List<City>

    @GET("/api/city/{id}")
    suspend fun getCityById(@Path("id") id: Int): City
}
