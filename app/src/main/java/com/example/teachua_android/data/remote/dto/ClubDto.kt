package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.club.Club
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ClubDto(
    val id: Int ,
    val name: String ,
    val description: String ,
    val urlLogo: String ,
    val urlBackground: String? ,
    val categories: List<CategoryDto> ,
    val contacts: List<ContactsDto> ,
    val locations: List<LocationDto> ,
    val rating: Float
) {
    data class Block(
        @SerializedName("key") val key: String ,
        @SerializedName("text") val text: String ,
        @SerializedName("type") val type: String ,
        @SerializedName("depth") val depth: Int ,
        @SerializedName("inlineStyleRanges") val inlineStyleRanges: List<Any> ,
        @SerializedName("entityRanges") val entityRanges: List<Any> ,
        @SerializedName("data") val data: Map<String , Any>
    )

    data class EntityMap(
        @SerializedName("type") val type: String ,
        @SerializedName("mutability") val mutability: String ,
        @SerializedName("data") val data: Map<String , String>
    )

    data class Root(
        @SerializedName("blocks") val blocks: List<Block> ,
        @SerializedName("entityMap") val entityMap: Map<String , EntityMap>
    )

    fun getClubDescription(jsonString: String): String? {
        val gson = Gson()
        val root = gson.fromJson(jsonString , Root::class.java)
        return root.blocks.find { it.text.isNotBlank() }?.text
    }
}

fun ClubDto.toClub(): Club {
    return Club(
        id ,
        name ,
        getClubDescription(description) ?: "Опис відсутінй.",
        urlLogo ,
        urlBackground ,
        categories.map { it.toCategory() } ,
        contacts.map { it.toContacts() } ,
        locations.map { it.toLocation() } ,
        rating
    )
}



