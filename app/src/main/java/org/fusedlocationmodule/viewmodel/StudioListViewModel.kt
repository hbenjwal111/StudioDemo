package org.fusedlocationmodule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fusedlocationmodule.api.NetworkResult
import org.fusedlocationmodule.model.StudioListRequest
import org.fusedlocationmodule.model.StudioResponse
import org.fusedlocationmodule.repo.StudioListRequestRepoImpl
import javax.inject.Inject


@HiltViewModel
class StudioListViewModel @Inject constructor(
    private val studioListRequestRepo: StudioListRequestRepoImpl
) : ViewModel() {

    private val _response =
        MutableStateFlow<NetworkResult<StudioResponse>>(NetworkResult.Loading())
    val response: StateFlow<NetworkResult<StudioResponse>> = _response.asStateFlow()

    fun fetchStudioList() {
        viewModelScope.launch {
            _response.value = NetworkResult.Loading()
            try {
                // Fetch studio list from repository
                val result = studioListRequestRepo.getStudioList()
                result.collect {
                    _response.value = it
                }
            } catch (e: Exception) {
                _response.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
