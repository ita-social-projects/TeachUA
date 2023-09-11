package com.example.teachua_android.domain.repository

import com.example.teachua_android.domain.model.Club.Club

interface ClubsRepository {
    suspend fun getClubs(): List<Club>
}