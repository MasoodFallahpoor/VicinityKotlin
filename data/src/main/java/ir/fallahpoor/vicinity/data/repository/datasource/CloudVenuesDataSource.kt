package ir.fallahpoor.vicinity.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.vicinity.data.WebServiceFactory
import ir.fallahpoor.vicinity.data.entity.VenueDetailsEntity
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.data.entity.VenuesEntity
import ir.fallahpoor.vicinity.data.repository.cache.VenuesCache
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class CloudVenuesDataSource @Inject
constructor(webServiceFactory: WebServiceFactory, private val venuesCache: VenuesCache) :
    VenuesDataSource {

    private val venuesWebService: VenuesWebService

    init {
        venuesWebService = webServiceFactory.createService(VenuesWebService::class.java)
    }

    override fun getVenues(latitude: Double, longitude: Double): Single<List<VenueEntity>> {

        val latLng = "$latitude,$longitude"

        return venuesWebService.getVenuesNear(latLng, CLIENT_ID, CLIENT_SECRET, VERSION)
            .doOnSuccess { venuesEntity ->
                venuesCache.replaceVenues(
                    latitude,
                    longitude,
                    venuesEntity.response.venues
                )
            }
            .map { venuesEntity -> venuesEntity.response.venues }

    }

    override fun getVenue(venueId: String): Single<VenueEntity> {
        return venuesWebService.getVenueDetails(venueId, CLIENT_ID, CLIENT_SECRET, VERSION)
            .doOnSuccess { venueDetailsEntity -> venuesCache.saveVenue(venueDetailsEntity.response.venue) }
            .map { venueDetailsEntity -> venueDetailsEntity.response.venue }
    }

    private interface VenuesWebService {
        @GET("search")
        fun getVenuesNear(
            @Query("ll") latLng: String, @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String, @Query("v") version: String
        ): Single<VenuesEntity>

        @GET("{venueId}")
        fun getVenueDetails(
            @Path("venueId") venueId: String, @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String, @Query("v") version: String
        ): Single<VenueDetailsEntity>
    }

    private companion object {
        private const val CLIENT_ID = "IJZDVIXBO10YGCTXHD0UV0QO2QUCW0L34J2DGUFQBGI2FV0X"
        private const val CLIENT_SECRET = "RLRAMRXMPEI0K22XTWTNP3ZFJ2REXTVIOV45UCLLCRFBZ12A"
        private const val VERSION = "20190601"
    }

}