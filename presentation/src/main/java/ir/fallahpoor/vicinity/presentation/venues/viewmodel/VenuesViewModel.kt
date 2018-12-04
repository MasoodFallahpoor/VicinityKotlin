package ir.fallahpoor.vicinity.presentation.venues.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ir.fallahpoor.vicinity.domain.interactor.GetVenuesUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionParser
import ir.fallahpoor.vicinity.presentation.venues.model.VenueModel
import ir.fallahpoor.vicinity.presentation.venues.model.VenuesDataMapper

class VenuesViewModel(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val venuesDataMapper: VenuesDataMapper,
    private val exceptionParser: ExceptionParser
) : ViewModel() {

    private companion object {
        private const val DISTANCE_THRESHOLD_IN_METERS = 100
    }

    val venuesLiveData = MutableLiveData<List<VenueModel>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()
    private var disposable: Disposable? = null
    private var prevLatitude: Double = 0.toDouble()
    private var prevLongitude: Double = 0.toDouble()

    fun getVenues(latitude: Double, longitude: Double) {
        if (shouldLoadVenues(latitude, longitude)) {
            setLoading(true)
            loadVenues(latitude, longitude)
        }
    }

    private fun shouldLoadVenues(latitude: Double, longitude: Double) =
        venuesLiveData.value == null || isDistanceThresholdCrossed(latitude, longitude)

    private fun isDistanceThresholdCrossed(latitude: Double, longitude: Double) =
        distanceInMeters(
            prevLatitude,
            prevLongitude,
            latitude,
            longitude
        ) > DISTANCE_THRESHOLD_IN_METERS

    private fun loadVenues(latitude: Double, longitude: Double) {

        disposable =
                getVenuesUseCase.execute(GetVenuesUseCase.Inputs.forLocation(latitude, longitude))
                    .doFinally { setLoading(false) }
                    .subscribe(
                        { venues ->
                            saveCurrentLocation(latitude, longitude)
                            venuesLiveData.value = venuesDataMapper.transformVenues(venues)
                        },
                        { throwable ->
                            errorLiveData.value = exceptionParser.parseException(throwable)
                        }
                    )

    }

    private fun saveCurrentLocation(latitude: Double, longitude: Double) {
        prevLatitude = latitude
        prevLongitude = longitude
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    private fun distanceInMeters(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Double {

        val radiusOfEarth = 6371

        val latDistance = Math.toRadians(latitude2 - latitude1)
        val lonDistance = Math.toRadians(longitude2 - longitude1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(
            Math.toRadians(latitude1)
        ) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = radiusOfEarth.toDouble() * c * 1000.0

        distance = Math.pow(distance, 2.0)

        return Math.sqrt(distance)

    }

    private fun setLoading(isLoading: Boolean) {
        loadingLiveData.value = isLoading
    }

}