package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.city.City

data class CityDto (
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

fun CityDto.toCity(): City {
    return City(id, name, latitude, longitude)
}