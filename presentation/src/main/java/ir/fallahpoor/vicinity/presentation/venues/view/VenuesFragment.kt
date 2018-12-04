package ir.fallahpoor.vicinity.presentation.venues.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import ir.fallahpoor.vicinity.BuildConfig
import ir.fallahpoor.vicinity.R
import ir.fallahpoor.vicinity.databinding.FragmentVenuesBinding
import ir.fallahpoor.vicinity.presentation.app.App
import ir.fallahpoor.vicinity.presentation.venues.di.DaggerVenuesComponent
import ir.fallahpoor.vicinity.presentation.venues.model.VenueModel
import ir.fallahpoor.vicinity.presentation.venues.viewmodel.VenuesViewModel
import ir.fallahpoor.vicinity.presentation.venues.viewmodel.VenuesViewModelFactory
import kotlinx.android.synthetic.main.fragment_venue_details.*
import kotlinx.android.synthetic.main.fragment_venues.*
import javax.inject.Inject

class VenuesFragment : Fragment() {

    private companion object {
        private const val TAG = "@@@@@@"
        private const val REQUEST_CHECK_SETTINGS = 100
        private const val REQUEST_CODE_ACCESS_FINE_LOCATION = 1000
        private const val UPDATE_INTERVAL_IN_MS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MS: Long = 5000
    }

    @Inject
    lateinit var venuesViewModelFactory: VenuesViewModelFactory

    private lateinit var binding: FragmentVenuesBinding
    private lateinit var locationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var venuesViewModel: VenuesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        binding = FragmentVenuesBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectDependencies()

        super.onActivityCreated(savedInstanceState)

        venuesViewModel = ViewModelProviders.of(this, venuesViewModelFactory)
            .get(VenuesViewModel::class.java)

        locationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        setupLocationCallback()

        subscribeToViewModel()

    }

    private fun injectDependencies() {
        DaggerVenuesComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    lastLocation = it.lastLocation
                    log("Location: " + lastLocation?.longitude + "," + lastLocation?.longitude)
                    venuesViewModel.getVenues(lastLocation!!.latitude, lastLocation!!.longitude)
                }
            }
        }
    }

    private fun subscribeToViewModel() {

        venuesViewModel.loadingLiveData.observe(
            this,
            Observer { show -> if (show) showLoading() else hideLoading() })

        venuesViewModel.errorLiveData.observe(
            this,
            Observer { errorMessage -> showError(errorMessage) }
        )

        venuesViewModel.venuesLiveData.observe(
            this,
            Observer { venuesList -> showVenues(venuesList) })

    }

    private fun showLoading() {
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.loading.loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE
    }

    private fun showError(errorMessage: String) {
        binding.tryAgain.errorMessageTextView.text = errorMessage
        binding.tryAgain.tryAgainButton.setOnClickListener {
            venuesViewModel.getVenues(lastLocation!!.latitude, lastLocation!!.longitude)
        }
        binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
        binding.venuesRecyclerView.visibility = View.GONE
    }

    private fun showVenues(venues: List<VenueModel>) {

        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.venuesRecyclerView.visibility = View.VISIBLE

        binding.venuesRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        binding.venuesRecyclerView.addItemDecoration(
            DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL)
        )
        binding.venuesRecyclerView.adapter = VenuesAdapter(activity!!, venues)

    }

    override fun onStart() {

        super.onStart()

        if (isLocationPermissionGranted()) {
            checkLocationSettings()
        } else {
            requestPermission()
        }

    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationSettings() {

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(getLocationRequest())
            .build()
        val settingsClient = LocationServices.getSettingsClient(activity!!)
        val task = settingsClient.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener(activity!!) {
            log("All location settings are satisfied. Start location updates...")
            startLocationUpdates()
        }

        task.addOnFailureListener(activity!!) { e ->
            val statusCode = (e as ApiException).statusCode
            when (statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    log("Location settings are not satisfied. Attempting to upgrade location settings")
                    try {
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(
                            activity!!,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sie: IntentSender.SendIntentException) {
                        log("PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    val errorMessage = "Location settings are inadequate, and cannot be fixed here."
                    Snackbar.make(
                        venuesRecyclerView,
                        errorMessage,
                        Snackbar.LENGTH_LONG
                    )
                }
            }

        }

    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_ACCESS_FINE_LOCATION
        )
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                log("Permission granted")
                checkLocationSettings()
            } else {
                // Permission is denied either temporarily or permanently.
                Snackbar.make(
                    venuesRecyclerView,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) { launchAppSettings() }
                    .show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationClient.requestLocationUpdates(
            getLocationRequest(),
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun getLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MS
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
    }

    private fun launchAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    log("User made required location settings changes.")
                    startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    log("Required location settings changes NOT made.")
                    Snackbar.make(
                        contentLayout,
                        R.string.location_disabled,
                        Snackbar.LENGTH_INDEFINITE
                    )
                }
            }
        }
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

}
