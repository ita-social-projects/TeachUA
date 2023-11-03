package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.club.ContactType

data class ContactTypeDto(
    val id: Int,
    val name: String,
    val urlLogo: String,
)

fun ContactTypeDto.toContactType(): ContactType {
    return ContactType(id, name, urlLogo)
}