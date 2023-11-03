package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.club.Location
import com.google.gson.annotations.SerializedName

data class LocationDto(

    @SerializedName("name")
    val name: String ,
    @SerializedName("address")
    val street: String
)

fun LocationDto.toLocation(): Location {
    return Location(name, street)
}
