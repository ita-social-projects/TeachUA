package com.example.teachua_android.domain.model.club


data class Club(
    val id: Int ,
    val name: String ,
    var description: String ,
    val urlLogo: String ,
    val urlBackground: String? ,
    val categories: List<Category> ,
    val contacts: List<Contacts> ,
    val locations: List<Location> ,
    val rating: Float
)


