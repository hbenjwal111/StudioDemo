package org.fusedlocationmodule.screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.fusedlocationmodule.adapter.StudioAdapter
import org.fusedlocationmodule.api.NetworkResult
import org.fusedlocationmodule.databinding.ActivityStudioListBinding
import org.fusedlocationmodule.model.Studio
import org.fusedlocationmodule.model.StudioResponse
import org.fusedlocationmodule.permission.PermissionUtils
import org.fusedlocationmodule.viewmodel.StudioListViewModel
import javax.inject.Inject


@AndroidEntryPoint
class StudioListActivity : AppCompatActivity() {
    @Inject
    lateinit var permissionUtils: PermissionUtils
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var adapter: StudioAdapter
    private lateinit var binding: ActivityStudioListBinding
    private val viewModel: StudioListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudioListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    initObserver()
                } else {
                    redirectToAppSettings()
                }
            }
        checkLocationPermission()
        adapter = StudioAdapter()
        setupRecyclerView()

    }

    private fun checkLocationPermission() {
        permissionUtils.checkAndRequestPermission(
            context = this,
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            onPermissionGranted = {

               initObserver()
            },
            permissionLauncher = locationPermissionLauncher
        )
    }

    private fun redirectToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.fetchStudioList()
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.response.collect {
                    handleNetworkResult(it)
                }
            }
        }
    }

    private fun handleNetworkResult(result: NetworkResult<StudioResponse>) {
        when (result) {
            is NetworkResult.Success -> {
                val studioResponse: StudioResponse? = result.data
                val studioList: List<Studio> = studioResponse?.result?.studio_list ?: emptyList()
                adapter.submitList(studioList)
            }
            is NetworkResult.Error -> {
                val error: String = result.message ?: "Unknown error"
                Log.e("NetworkError", "Error occurred: $error")
            }
            is NetworkResult.Loading -> {
                // Handle loading state if needed
            }
            else -> {
                // Handle other cases if needed
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}