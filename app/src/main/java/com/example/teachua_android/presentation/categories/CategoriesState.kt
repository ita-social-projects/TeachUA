package com.example.teachua_android.presentation.categories

import com.example.teachua_android.domain.model.club.Category

data class CategoriesState (
    var isLoading: Boolean,
    var categories: List<Category>,
    var error: String = ""
)