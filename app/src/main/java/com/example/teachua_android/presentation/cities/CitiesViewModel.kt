package com.example.teachua_android.presentation.cities

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachua_android.common.Constants
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.use_case.cities.get_cities.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase ,
) : ViewModel() {

    private var _citiesState by mutableStateOf(CitiesState(false , emptyList() , null , ""))
    val citiesState get() = _citiesState


    init {
        collectCities()
    }

    private fun collectCities() {
        Log.d(Constants.TAG , "collectCitites()")
        viewModelScope.launch {
            getCitiesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d(Constants.TAG , "collectCitites(): resource loading")
                        _citiesState.isLoading = true
                    }

                    is Resource.Success -> {
                        Log.d(Constants.TAG , "collectCitites(): resource success")

                        _citiesState = _citiesState.copy(
                            isLoading = false ,
                            cities = resource.data ?: emptyList() ,
                            currentCity = resource.data?.first() ,
                            error = ""
                        )
                    }

                    is Resource.Error -> {
                        Log.d(
                            Constants.TAG , "collectCitites(): resource error: ${resource.message}"
                        )

                        _citiesState.error = resource.message ?: ""
                        _citiesState.isLoading = false
                    }

                }
            }
        }
    }
}