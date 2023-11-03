package com.example.teachua_android.presentation.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.use_case.categories.get_categories.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
): ViewModel(){
    private var _categoriesState by mutableStateOf(CategoriesState(false , emptyList()))
    val categoriesState get() = _categoriesState

    init {
        collectCategories()
    }

    private fun collectCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _categoriesState.isLoading = true
                    }

                    is Resource.Success -> {
                        _categoriesState = _categoriesState.copy(
                            false , resource.data ?: emptyList() , ""
                        )
                    }

                    is Resource.Error -> {
                        _categoriesState.isLoading = false
                        _categoriesState.error = resource.message ?: ""
                    }

                }
            }
        }
    }

}