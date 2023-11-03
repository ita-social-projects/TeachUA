package com.example.teachua_android.data.remote

import com.example.teachua_android.data.remote.dto.ClubDto
import retrofit2.http.GET

interface ClubsApi {
    @GET("api/clubs")
    suspend fun getClubs(): List<ClubDto>
}