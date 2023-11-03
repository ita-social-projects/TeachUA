package com.example.teachua_android.data.remote

import com.example.teachua_android.data.remote.dto.CategoryDto
import retrofit2.http.GET

interface CategoryApi {
    @GET("api/categories")
    suspend fun getCategories(): List<CategoryDto>
}