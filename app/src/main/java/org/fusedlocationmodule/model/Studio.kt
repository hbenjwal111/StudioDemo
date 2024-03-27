package org.fusedlocationmodule.model

data class Studio(
    val activity_list: MutableList<Activity> = mutableListOf(),
    val average_rating: String? = null,
    val description: String? = null,
    val distance: String? = null,
    val fulladdress: String? = null,
    val latitude: String? = null,
    val locality_name: String? = null,
    val longitude: String? = null,
    val profile_image: String? = null,
    val public_url: String? = null,
    val studio_app_name: String? = null,
    val studio_id: Int? = null,
    val studio_logo: String? = null,
    val studio_name: String? = null,
    val total_reviews: String? = null
)