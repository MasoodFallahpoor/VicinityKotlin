package ir.fallahpoor.vicinity.presentation.venues.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel

interface VenuesView : MvpView {

    fun showLoading()

    fun hideLoading()

    fun showError(errorMessage: String)

    fun showVenues(venues: List<VenueViewModel>)

}