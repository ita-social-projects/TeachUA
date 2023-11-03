package com.example.teachua_android.presentation.clubs

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teachua_android.common.Constants
import com.example.teachua_android.common.Resource
import com.example.teachua_android.domain.use_case.clubs.get_clubs.GetClubsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClubsViewModel @Inject constructor(
    private val getClubsUseCase: GetClubsUseCase
) : ViewModel() {
    private var _clubsState = mutableStateOf(ClubsState(emptyList()))
    val clubsState get() = _clubsState

    init {
        collectClubs()
    }

    private fun collectClubs() {
        viewModelScope.launch {
            getClubsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d(Constants.TAG , "collectClubs(): resource loading")
                        _clubsState.value.isLoading = true
                    }

                    is Resource.Success -> {
                        _clubsState.value = _clubsState.value.copy(
                            clubs = resource.data ?: emptyList() ,
                            isLoading = false ,
                            error = ""
                        )
                    }

                    is Resource.Error -> {
                        _clubsState.value.error = resource.message ?: ""
                        _clubsState.value.isLoading = false
                    }
                }
            }
        }
    }
}