package ir.fallahpoor.vicinity.presentation.venuedetails.view

import com.hannesdorfmann.mosby3.mvp.MvpView
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel

interface VenueDetailsView : MvpView {

    fun showLoading()

    fun hideLoading()

    fun showError(errorMessage: String)

    fun showVenue(venue: VenueViewModel)

}