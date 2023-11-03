package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.club.Contacts

data class ContactsDto(
    val contactType: ContactTypeDto,
    val contactData: String
)

fun ContactsDto.toContacts(): Contacts{
    return Contacts(contactType.toContactType(), contactData)
}