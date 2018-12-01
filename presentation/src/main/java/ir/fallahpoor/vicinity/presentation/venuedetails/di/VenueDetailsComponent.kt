package ir.fallahpoor.vicinity.presentation.venuedetails.di

import dagger.Component
import ir.fallahpoor.vicinity.presentation.app.AppComponent
import ir.fallahpoor.vicinity.presentation.venuedetails.view.VenueDetailsFragment

@Component(dependencies = [AppComponent::class], modules = [VenueDetailsModule::class])
interface VenueDetailsComponent {
    fun inject(venueDetailsFragment: VenueDetailsFragment)
}