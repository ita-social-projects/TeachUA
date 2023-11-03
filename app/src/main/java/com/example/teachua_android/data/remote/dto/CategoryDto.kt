package com.example.teachua_android.data.remote.dto

import com.example.teachua_android.domain.model.club.Category

data class CategoryDto(
    var id: Int ,
    var sortBy: Int ,
    var name: String ,
    var description: String ,
    var urlLogo: String ,
    var backgroundColor: String ,
    var tagBackgroundColor: String ,
    var tagTextColor: String
)

fun CategoryDto.toCategory(): Category {
    return Category(
        id ,
        sortBy ,
        name ,
        description ,
        urlLogo ,
        backgroundColor ,
        tagBackgroundColor ,
        tagTextColor
    )
}