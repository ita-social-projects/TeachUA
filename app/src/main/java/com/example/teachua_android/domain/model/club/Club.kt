package com.example.teachua_android.domain.model.club

import com.example.teachua_android.data.remote.dto.ContactsDto

data class Club(
    var clubId: Int,
    var clubName: String,
    var clubDescription: String,
    var clubImage: String,
    var clubBackgroundColor: String,
    var clubCategoryName: String,
    var clubRating: Float,
    var clubContacts: List<ContactsDto>,
    var clubBanner: String?
)
