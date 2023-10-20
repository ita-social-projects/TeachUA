package com.example.teachua_android.domain.use_case.cities.get_cities

import android.util.Log
import com.example.teachua_android.common.Constants
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.model.city.City
import com.example.teachua_android.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CitiesRepository
) {
    operator fun invoke(): Flow<Resource<List<City>>> = flow {
        try {
            emit(Resource.Loading<List<City>>())
            val cities = repository.getCities()
            emit(Resource.Success<List<City>>(cities))
        } catch (e: HttpException) {
            emit(Resource.Error<List<City>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            Log.d(Constants.TAG, "Exception in USE CASE ${e.message}")
            emit(Resource.Error<List<City>>("Couldn't reach server. Check your internet connection."))
        }
    }
}