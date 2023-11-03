package com.example.teachua_android.domain.use_case.clubs.get_clubs

import android.util.Log
import com.example.teachua_android.common.Constants
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.model.club.Club
import com.example.teachua_android.domain.repository.ClubsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class GetClubsUseCase @Inject constructor(
    private val clubsRepository: ClubsRepository
) {
    operator fun invoke(): Flow<Resource<List<Club>>> = flow {
        try {
            emit(Resource.Loading<List<Club>>())
            val clubs = clubsRepository.getClubs()

            emit(Resource.Success<List<Club>>(clubs))

        } catch (e: HttpException) {
            emit(Resource.Error<List<Club>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: java.io.IOException) {
            Log.d(Constants.TAG , "Exception in Clubs USE CASE ${e.message}")
            emit(Resource.Error<List<Club>>("Couldn't reach server. Check your internet connection."))
        }
    }
}