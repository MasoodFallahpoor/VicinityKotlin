package ir.fallahpoor.vicinity.venuedetails.model

import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.venues.model.LocationViewModel
import ir.fallahpoor.vicinity.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsDataMapper @Inject
constructor() {

    fun transform(venue: Venue): VenueViewModel {
        return VenueViewModel(venue.id, venue.name, transformLocation(venue.location))
    }

    private fun transformLocation(location: Location): LocationViewModel {
        return LocationViewModel(location.address, location.latitude, location.longitude)
    }

}