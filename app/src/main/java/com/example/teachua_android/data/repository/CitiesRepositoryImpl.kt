package com.example.teachua_android.data.repository

import android.util.Log
import com.example.teachua_android.common.Constants.TAG
import com.example.teachua_android.data.remote.CitiesApi
import com.example.teachua_android.data.remote.dto.toCity
import com.example.teachua_android.domain.model.city.City
import com.example.teachua_android.domain.repository.CitiesRepository
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val api: CitiesApi
) : CitiesRepository {

    override suspend fun getCities(): List<City> {
        val a = api.getCities().map { it.toCity() }
        Log.d(TAG, "Api.GetCities: $a")
        return a
    }

    override suspend fun getCityById(id: Int): City {
        return api.getCityById(id).toCity()
    }
}