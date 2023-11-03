package com.example.teachua_android.domain.repository

import com.example.teachua_android.domain.model.club.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}