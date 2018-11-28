package ir.fallahpoor.vicinity.data.repository

import io.reactivex.Single
import ir.fallahpoor.vicinity.data.mapper.VenuesEntityDataMapper
import ir.fallahpoor.vicinity.data.repository.datasource.VenuesDataSourceFactory
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository

class VenuesRepositoryImpl(
    private val venuesDataSourceFactory: VenuesDataSourceFactory,
    private val venuesEntityDataMapper: VenuesEntityDataMapper
) : VenuesRepository {

    override fun getVenuesAround(latitude: Double, longitude: Double): Single<List<Venue>> {
        return venuesDataSourceFactory.create(latitude, longitude)
            .getVenues(latitude, longitude)
            .map(venuesEntityDataMapper::transform)
    }

    override fun getVenueDetails(venueId: String): Single<Venue> {
        return venuesDataSourceFactory.create(venueId)
            .getVenue(venueId)
            .map(venuesEntityDataMapper::transformVenue);
    }

}