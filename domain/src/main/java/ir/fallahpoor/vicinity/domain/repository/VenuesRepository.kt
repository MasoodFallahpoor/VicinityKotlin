package ir.fallahpoor.vicinity.domain.repository

import io.reactivex.Single
import ir.fallahpoor.vicinity.domain.model.Venue

interface VenuesRepository {
    fun getVenuesAround(latitude: Double, longitude: Double): Single<List<Venue>>
    fun getVenueDetails(venueId: String): Single<Venue>
}