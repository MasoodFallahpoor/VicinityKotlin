package ir.fallahpoor.vicinity.venues.presenter

import android.util.Log
import ir.fallahpoor.vicinity.BaseMvpPresenter
import ir.fallahpoor.vicinity.common.ExceptionHandler
import ir.fallahpoor.vicinity.domain.interactor.GetVenuesUseCase
import ir.fallahpoor.vicinity.venues.model.VenuesDataMapper
import ir.fallahpoor.vicinity.venues.view.VenuesView

class VenuesPresenterImpl(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val venuesDataMapper: VenuesDataMapper,
    private val exceptionHandler: ExceptionHandler
) : BaseMvpPresenter<VenuesView>(), VenuesPresenter {

    private val MAX_DISTANCE_TO_UPDATE_VENUES = 100
    private var prevLatitude: Double = 0.toDouble()
    private var prevLongitude: Double = 0.toDouble()

    override fun getVenuesAround(latitude: Double, longitude: Double) {

        // If the distance between previous location and current one is less that
        // a threshold then simply do nothing.
        if (distanceInMeters(prevLatitude, prevLongitude, latitude, longitude) <=
            MAX_DISTANCE_TO_UPDATE_VENUES
        ) {
            Log.d("@@@@@@", "returning")
            return
        }

        ifViewAttached { it.showLoading() }

        val d = getVenuesUseCase.execute(GetVenuesUseCase.Inputs.forLocation(latitude, longitude))
            .subscribe(
                { venues ->
                    prevLatitude = latitude
                    prevLongitude = longitude
                    ifViewAttached { view ->
                        view.hideLoading()
                        view.showVenues(venuesDataMapper.transformVenues(venues))
                    }
                },
                { throwable ->
                    ifViewAttached { view ->
                        val error = exceptionHandler.parseException(throwable)
                        view.hideLoading()
                        view.showError(error.message)
                    }
                }
            )
        addDisposable(d)

    }

    override fun detachView() {
        super.detachView()
        prevLatitude = 0.toDouble()
        prevLongitude = 0.toDouble()
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

}