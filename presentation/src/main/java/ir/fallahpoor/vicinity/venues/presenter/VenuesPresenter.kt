package ir.fallahpoor.vicinity.venues.presenter

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import ir.fallahpoor.vicinity.venues.view.VenuesView

interface VenuesPresenter : MvpPresenter<VenuesView> {

    fun getVenuesAround(latitude: Double, longitude: Double)

}