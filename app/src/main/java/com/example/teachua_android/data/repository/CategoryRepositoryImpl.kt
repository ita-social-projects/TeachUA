package com.example.teachua_android.data.repository

import android.util.Log
import com.example.teachua_android.common.Constants
import com.example.teachua_android.data.remote.CategoryApi
import com.example.teachua_android.data.remote.dto.toCategory
import com.example.teachua_android.domain.model.club.Category
import com.example.teachua_android.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryApi
): CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        val categories = api.getCategories().map { it.toCategory() }
        Log.d(Constants.TAG, "$categories")
        return categories
    }
}