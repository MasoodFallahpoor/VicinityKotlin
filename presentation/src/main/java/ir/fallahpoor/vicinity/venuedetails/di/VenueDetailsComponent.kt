package ir.fallahpoor.vicinity.venuedetails.di

import dagger.Component
import ir.fallahpoor.vicinity.venuedetails.view.VenueDetailsActivity
import ir.fallahpoor.vicinity.app.AppComponent

@Component(dependencies = [AppComponent::class], modules = [VenueDetailsModule::class])
interface VenueDetailsComponent {
    fun inject(venueDetailsActivity: VenueDetailsActivity)
}