package ir.fallahpoor.vicinity.data.repository.datasource

import ir.fallahpoor.vicinity.data.repository.cache.VenuesCache
import javax.inject.Inject

class VenuesDataSourceFactory @Inject
internal constructor(
    private val venuesCache: VenuesCache,
    private val localVenuesDataStore: LocalVenuesDataSource,
    private val cloudVenuesDataStore: CloudVenuesDataSource
) {

    fun create(latitude: Double, longitude: Double): VenuesDataSource {
        return if (venuesCache.venuesExistFor(latitude, longitude)) {
            localVenuesDataStore
        } else {
            cloudVenuesDataStore
        }
    }

    fun create(venueId: String): VenuesDataSource {
        return if (venuesCache.venueExists(venueId)) {
            localVenuesDataStore
        } else {
            cloudVenuesDataStore
        }
    }

}