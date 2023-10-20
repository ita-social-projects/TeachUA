package com.example.teachua_android.presentation.cities

import com.example.teachua_android.domain.model.city.City

data class CitiesState(
    var isLoading: Boolean = false ,
    var cities: List<City> ,
    var currentCity: City? ,
    var error: String
)
