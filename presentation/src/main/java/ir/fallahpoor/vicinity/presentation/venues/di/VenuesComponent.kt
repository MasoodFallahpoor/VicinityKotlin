package ir.fallahpoor.vicinity.presentation.venues.di

import dagger.Component
import ir.fallahpoor.vicinity.presentation.app.AppComponent
import ir.fallahpoor.vicinity.presentation.venues.view.VenuesFragment

@Component(dependencies = [AppComponent::class], modules = [VenuesModule::class])
interface VenuesComponent {
    fun inject(venuesFragment: VenuesFragment)
}