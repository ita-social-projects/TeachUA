package com.example.teachua_android.presentation.clubs

import com.example.teachua_android.domain.model.club.Club

data class ClubsState(
    var clubs: List<Club> ,
    var isLoading: Boolean = false ,
    var error: String = ""
)