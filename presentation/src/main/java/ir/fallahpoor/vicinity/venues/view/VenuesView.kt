package ir.fallahpoor.vicinity.venues.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import ir.fallahpoor.vicinity.venues.model.VenueViewModel

interface VenuesView : MvpView {

    fun showLoading()

    fun hideLoading()

    fun showError(errorMessage: String)

    fun showVenues(venues: List<VenueViewModel>)

}