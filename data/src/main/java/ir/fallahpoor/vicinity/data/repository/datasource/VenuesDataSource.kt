package ir.fallahpoor.vicinity.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.vicinity.data.entity.VenueEntity

interface VenuesDataSource {
    fun getVenues(latitude: Double, longitude: Double): Single<List<VenueEntity>>

    fun getVenue(venueId: String): Single<VenueEntity>
}