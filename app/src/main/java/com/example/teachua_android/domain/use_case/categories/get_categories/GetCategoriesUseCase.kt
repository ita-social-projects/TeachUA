package com.example.teachua_android.domain.use_case.categories.get_categories

import android.util.Log
import com.example.teachua_android.common.Constants
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.model.club.Category
import com.example.teachua_android.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading<List<Category>>())
            val categories = categoryRepository.getCategories()
            emit(Resource.Success<List<Category>>(categories))

        } catch (e: HttpException) {
            emit(
                Resource.Error<List<Category>>(
                    e.localizedMessage ?: "An unexpected error occured"
                )
            )
        } catch (e: IOException) {
            Log.d(Constants.TAG , "Exception in Categories USE CASE ${e.message}")
            emit(Resource.Error<List<Category>>("Couldn't reach server. Check your internet connection."))
        }
    }
}