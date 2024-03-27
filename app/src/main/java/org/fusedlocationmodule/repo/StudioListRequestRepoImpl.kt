package org.fusedlocationmodule.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.fusedlocationmodule.api.BaseApiResponse
import org.fusedlocationmodule.api.NetworkResult
import org.fusedlocationmodule.api.StudioService
import org.fusedlocationmodule.model.StudioListRequest
import org.fusedlocationmodule.model.StudioResponse
import javax.inject.Inject

class StudioListRequestRepoImpl @Inject constructor(
    private val locationRepo: LocationRepo,
    private val studioService: StudioService,
) : BaseApiResponse() {
   suspend fun getStudioList(): Flow<NetworkResult<StudioResponse>> {
        return flow {
            // Get current location
            val location = locationRepo.getCurrentLocation()

            // Send location to server along with request
            val requestWithLocation = StudioListRequest(
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString(),
                request_url_string = "",
                page_number = 1,
                page_name = "request.page_name"
            )
            emit(safeApiCall { studioService.getStudioList(requestWithLocation) })
        }.flowOn(Dispatchers.IO)
    }
}
