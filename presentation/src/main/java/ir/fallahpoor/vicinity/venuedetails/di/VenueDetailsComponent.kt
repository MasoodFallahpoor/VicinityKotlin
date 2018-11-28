package ir.fallahpoor.vicinity.venuedetails.di

import dagger.Component
import ir.fallahpoor.vicinity.app.AppComponent
import ir.fallahpoor.vicinity.venuedetails.view.VenueDetailsFragment

@Component(dependencies = [AppComponent::class], modules = [VenueDetailsModule::class])
interface VenueDetailsComponent {
    fun inject(venueDetailsFragment: VenueDetailsFragment)
}