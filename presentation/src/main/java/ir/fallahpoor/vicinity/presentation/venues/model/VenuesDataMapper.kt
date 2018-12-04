package ir.fallahpoor.vicinity.presentation.venues.model

import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import java.util.*
import javax.inject.Inject

class VenuesDataMapper @Inject
constructor() {

    fun transformVenues(venues: List<Venue>?): List<VenueModel> {

        val venueModels: MutableList<VenueModel>

        if (venues != null && venues.isNotEmpty()) {
            venueModels = ArrayList()
            for (venue in venues) {
                venueModels.add(transformVenue(venue))
            }
        } else {
            venueModels = Collections.emptyList()
        }

        return venueModels

    }

    private fun transformVenue(venue: Venue?): VenueModel {
        venue?.let {
            return VenueModel(it.id, it.name, transformLocation(it.location))
        }
    }

    private fun transformLocation(location: Location) =
        LocationModel(location.address, location.latitude, location.longitude)

}