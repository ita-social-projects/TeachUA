package com.example.teachua_android.data.remote

import com.example.teachua_android.data.remote.dto.CityDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CitiesApi {
    @GET("api/cities")
    suspend fun getCities(): List<CityDto>

    @GET("/api/city/{id}")
    suspend fun getCityById(@Path("id") id: Int): CityDto
}
