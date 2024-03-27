package org.fusedlocationmodule.model

import com.google.gson.annotations.SerializedName

data class StudioListRequest(
    @SerializedName("latitude")
    val latitude: String? = null,
    @SerializedName("longitude")
    val longitude: String? = null,
    @SerializedName("request_url_string")
    val request_url_string: String? = null,
    @SerializedName("page_number")
    val page_number: Int = 1,
    @SerializedName("page_name")
    val page_name: String? = null,
)
