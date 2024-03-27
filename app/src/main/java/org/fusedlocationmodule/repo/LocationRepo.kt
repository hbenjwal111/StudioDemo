package org.fusedlocationmodule.repo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import org.fusedlocationmodule.permission.PermissionUtils
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepo @Inject constructor(
    private val permissionUtils: PermissionUtils,
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location {
        return suspendCancellableCoroutine { continuation ->
            if (permissionUtils.isPermissionGranted(context.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            continuation.resume(location)
                        } else {
                            continuation.resumeWithException(NullPointerException("Last known location is null"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } else {
                continuation.resumeWithException(SecurityException("Location permission not granted"))
            }
        }
    }
}
