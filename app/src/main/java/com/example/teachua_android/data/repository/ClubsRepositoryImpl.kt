package com.example.teachua_android.data.repository

import android.util.Log
import com.example.teachua_android.common.Constants
import com.example.teachua_android.data.remote.ClubsApi
import com.example.teachua_android.data.remote.dto.toClub
import com.example.teachua_android.domain.model.club.Club
import com.example.teachua_android.domain.repository.ClubsRepository
import javax.inject.Inject

class ClubsRepositoryImpl @Inject constructor(
    private val api: ClubsApi
) : ClubsRepository {

    override suspend fun getClubs(): List<Club> {
        val clubs = api.getClubs()
        Log.d(Constants.TAG, "Api.getClubs(): ${clubs.filter{it.id == 26}}")
        return clubs.map { it.toClub() }
    }
}