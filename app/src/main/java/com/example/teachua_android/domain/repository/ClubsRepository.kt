package com.example.teachua_android.domain.repository

import com.example.teachua_android.domain.model.club.Club

interface ClubsRepository {
    suspend fun getClubs(): List<Club>
}

