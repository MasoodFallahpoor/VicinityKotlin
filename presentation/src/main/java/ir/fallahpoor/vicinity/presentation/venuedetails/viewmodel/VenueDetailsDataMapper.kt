package ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel

import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.presentation.venues.model.LocationModel
import ir.fallahpoor.vicinity.presentation.venues.model.VenueModel
import javax.inject.Inject

class VenueDetailsDataMapper @Inject
constructor() {

    fun transform(venue: Venue) =
        VenueModel(venue.id, venue.name, transformLocation(venue.location))

    private fun transformLocation(location: Location) =
        LocationModel(location.address, location.latitude, location.longitude)

}