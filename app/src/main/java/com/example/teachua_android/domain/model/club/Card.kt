package com.example.teachua_android.domain.model.club

import androidx.annotation.DrawableRes

data class Card(
    val title: String,
    val subtext: String,
    @DrawableRes val image: Int
)