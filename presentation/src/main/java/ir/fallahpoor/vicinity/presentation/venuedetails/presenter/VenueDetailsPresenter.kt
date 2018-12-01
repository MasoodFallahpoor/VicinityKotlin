package ir.fallahpoor.vicinity.presentation.venuedetails.presenter

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import ir.fallahpoor.vicinity.presentation.venuedetails.view.VenueDetailsView

interface VenueDetailsPresenter : MvpPresenter<VenueDetailsView> {
    fun getVenueDetails(venueId: String)
}