package ir.fallahpoor.vicinity.data.repository.cache

import io.reactivex.Single
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.data.entity.Venues
import ir.fallahpoor.vicinity.data.repository.dao.VenuesDao
import javax.inject.Inject

class VenuesCache @Inject
constructor(private val venuesDao: VenuesDao) {

    fun getVenues(): Single<List<VenueEntity>> {
        return venuesDao.getVenuesAsSingle().map(({ it.venues }))
    }

    fun venueExists(id: String): Boolean {
        return (venuesDao.venueExists(id) > 0)
    }

    fun saveVenue(venueEntity: VenueEntity) {
        venuesDao.saveVenue(venueEntity)
    }

    fun replaceVenues(latitude: Double, longitude: Double, venues: List<VenueEntity>) {
        val v = Venues(latitude, longitude, venues)
        venuesDao.deleteVenues()
        venuesDao.saveVenues(v)
    }

    fun getVenue(id: String): Single<VenueEntity> {
        return venuesDao.getVenue(id)
    }

    fun venuesExistFor(latitude: Double, longitude: Double): Boolean {
        return cacheNotEmpty() && areVenuesRelevantTo(latitude, longitude)
    }

    private fun cacheNotEmpty(): Boolean {
        return (venuesDao.getNumberOfVenues() != 0)
    }

    private fun areVenuesRelevantTo(latitude: Double, longitude: Double): Boolean {
        val venues = venuesDao.getVenues()
        return distanceInMeters(latitude, longitude, venues.latitude, venues.longitude) <= 100
    }

    private fun distanceInMeters(
        latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double
    ): Double {

        val radiusOfEarth = 6371

        val latDistance = Math.toRadians(latitude2 - latitude1)
        val lonDistance = Math.toRadians(longitude2 - longitude1)
        val a = Math.sin(latDistance / 2) *
                Math.sin(latDistance / 2) +
                (Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = radiusOfEarth.toDouble() * c * 1000.0

        distance = Math.pow(distance, 2.0)

        return Math.sqrt(distance)

    }

}