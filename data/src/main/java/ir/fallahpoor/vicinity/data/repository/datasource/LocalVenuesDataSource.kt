package ir.fallahpoor.vicinity.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.data.repository.cache.VenuesCache
import javax.inject.Inject

class LocalVenuesDataSource @Inject
constructor(private val venuesCache: VenuesCache) : VenuesDataSource {

    override fun getVenues(latitude: Double, longitude: Double): Single<List<VenueEntity>> {
        return venuesCache.getVenues()
    }

    override fun getVenue(venueId: String): Single<VenueEntity> {
        return venuesCache.getVenue(venueId)
    }

}