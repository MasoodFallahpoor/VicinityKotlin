package ir.fallahpoor.vicinity.presentation.venuedetails.model

import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.presentation.venues.model.LocationViewModel
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsDataMapper @Inject
constructor() {

    fun transform(venue: Venue) =
        VenueViewModel(venue.id, venue.name, transformLocation(venue.location))

    private fun transformLocation(location: Location) =
        LocationViewModel(location.address, location.latitude, location.longitude)

}