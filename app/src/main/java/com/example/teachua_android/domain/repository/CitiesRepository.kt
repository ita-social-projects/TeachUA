package com.example.teachua_android.domain.repository

import com.example.teachua_android.domain.model.city.City

interface CitiesRepository {

    suspend fun getCities(): List<City>

    suspend fun getCityById(id: Int): City
}