package ir.fallahpoor.vicinity.venues.model

import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import java.util.*
import javax.inject.Inject

class VenuesDataMapper @Inject
constructor() {

    fun transformVenues(venues: List<Venue>?): List<VenueViewModel> {

        val venueViewModels: MutableList<VenueViewModel>

        if (venues != null && venues.isNotEmpty()) {
            venueViewModels = ArrayList()
            for (venue in venues) {
                venueViewModels.add(transformVenue(venue))
            }
        } else {
            venueViewModels = Collections.emptyList()
        }

        return venueViewModels

    }

    private fun transformVenue(venue: Venue?): VenueViewModel {
        venue?.let {
            return VenueViewModel(it.id, it.name, transformLocation(it.location))
        }
    }

    private fun transformLocation(location: Location): LocationViewModel {
        return LocationViewModel(location.address, location.latitude, location.longitude)
    }

}