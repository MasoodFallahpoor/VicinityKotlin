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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import ir.fallahpoor.vicinity.BuildConfig
import ir.fallahpoor.vicinity.R
import ir.fallahpoor.vicinity.databinding.FragmentVenuesBinding
import ir.fallahpoor.vicinity.presentation.app.App
import ir.fallahpoor.vicinity.presentation.venues.di.DaggerVenuesComponent
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import ir.fallahpoor.vicinity.presentation.venues.presenter.VenuesPresenter
import kotlinx.android.synthetic.main.fragment_venue_details.*
import javax.inject.Inject

class VenuesFragment : MvpFragment<VenuesView, VenuesPresenter>(), VenuesView {

    private companion object {
        private const val TAG = "@@@@@@"
        private const val REQUEST_CHECK_SETTINGS = 100
        private const val REQUEST_CODE_ACCESS_FINE_LOCATION = 1000
        private const val UPDATE_INTERVAL_IN_MS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MS: Long = 5000
    }

    @Inject
    lateinit var venuesPresenter: VenuesPresenter

    private lateinit var binding: FragmentVenuesBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        binding = ir.fallahpoor.vicinity.databinding.FragmentVenuesBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        setupLocationCallback()

    }

    private fun injectDependencies() {
        DaggerVenuesComponent.builder()
            .appComponent((activity?.application as App).getAppComponent())
            .build()
            .inject(this)
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    log("Location: NULL")
                } else {
                    lastLocation = locationResult.lastLocation
                    log("Location: " + lastLocation?.longitude + "," + lastLocation?.longitude)
                    getPresenter().getVenuesAround(
                        lastLocation!!.latitude,
                        lastLocation!!.longitude
                    )
                }
            }
        }
    }

    override fun onStart() {

        super.onStart()

        if (isAccessFineLocationPermissionGranted()) {
            checkLocationSettings()
        } else {
            requestPermission()
        }

    }

    private fun isAccessFineLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_ACCESS_FINE_LOCATION
        )
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
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
                    contentLayout,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.settings) { launchAppSettings() }
                    .show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationSettings() {

        val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
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
                        contentLayout,
                        errorMessage,
                        Snackbar.LENGTH_LONG
                    )
                }
            }

        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
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

    override fun createPresenter(): VenuesPresenter {
        return venuesPresenter
    }

    override fun showLoading() {
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.loading.loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        binding.tryAgain.errorMessageTextView.text = errorMessage
        binding.tryAgain.tryAgainButton.setOnClickListener { _ ->
            lastLocation?.let {
                getPresenter().getVenuesAround(it.latitude, it.longitude)
            }
        }
        binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
        binding.venuesRecyclerView.visibility = View.GONE
    }

    override fun showVenues(venues: List<VenueViewModel>) {

        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.venuesRecyclerView.visibility = View.VISIBLE

        binding.venuesRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        binding.venuesRecyclerView.addItemDecoration(
            DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL)
        )
        val venuesAdapter = VenuesAdapter(activity!!, venues)
        binding.venuesRecyclerView.adapter = venuesAdapter

    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

}
