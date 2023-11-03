package com.example.teachua_android.domain.model.club

data class Category (
    var id: Int,
    var sortBy: Int,
    var name: String,
    var description: String,
    var urlLogo: String,
    var backgroundColor: String,
    var tagBackgroundColor: String,
    var tagTextColor: String
)

