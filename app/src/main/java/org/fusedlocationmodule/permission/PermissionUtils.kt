package org.fusedlocationmodule.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import javax.inject.Inject

class PermissionUtils @Inject constructor() {

    /**
     * Check and request permission
     *
     * @param fragment
     * @param permission
     * @param onPermissionGranted
     * @param permissionLauncher
     * @receiver
     */
    fun checkAndRequestPermission(
        context: Context,
        permission: String,
        onPermissionGranted: () -> Unit,
        permissionLauncher: ActivityResultLauncher<String>
    ) {
        val isLocationPermission = permission == Manifest.permission.ACCESS_FINE_LOCATION

        val allPermissionsGranted =
            (isLocationPermission && isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION))

        if (allPermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    /**
     * Is permission granted
     *
     * @param fragment
     * @param permission
     * @return
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
