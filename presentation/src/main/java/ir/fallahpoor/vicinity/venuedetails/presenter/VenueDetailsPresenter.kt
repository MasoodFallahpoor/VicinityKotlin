package ir.fallahpoor.vicinity.venuedetails.presenter

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import ir.fallahpoor.vicinity.venuedetails.view.VenueDetailsView

interface VenueDetailsPresenter : MvpPresenter<VenueDetailsView> {
    fun getVenueDetails(venueId: String)
}