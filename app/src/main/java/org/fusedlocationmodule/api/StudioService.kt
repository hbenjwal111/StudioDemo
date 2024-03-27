package org.fusedlocationmodule.api

import org.fusedlocationmodule.model.StudioListRequest
import org.fusedlocationmodule.model.StudioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StudioService {

    @POST("studio/list")
    suspend fun getStudioList(
        @Body request: StudioListRequest
    ): Response<StudioResponse>
}
