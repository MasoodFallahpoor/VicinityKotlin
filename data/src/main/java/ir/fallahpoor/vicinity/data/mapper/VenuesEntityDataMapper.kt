package ir.fallahpoor.vicinity.data.mapper

import ir.fallahpoor.vicinity.data.entity.LocationEntity
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.domain.model.Location
import ir.fallahpoor.vicinity.domain.model.Venue
import java.util.*
import javax.inject.Inject

class VenuesEntityDataMapper @Inject
constructor() {

    fun transform(venueEntities: List<VenueEntity>): List<Venue> {

        val venues: MutableList<Venue> = ArrayList()

        for (venue in venueEntities) {
            venues.add(transformVenue(venue))
        }

        return venues

    }

    fun transformVenue(venueEntity: VenueEntity) =
        Venue(venueEntity.id, venueEntity.name, transformLocation(venueEntity.location))

    private fun transformLocation(locationEntity: LocationEntity) =
        Location(locationEntity.address, locationEntity.latitude, locationEntity.longitude)

}