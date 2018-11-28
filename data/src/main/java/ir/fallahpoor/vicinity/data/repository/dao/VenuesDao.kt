package ir.fallahpoor.vicinity.data.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ir.fallahpoor.vicinity.data.entity.VenueEntity
import ir.fallahpoor.vicinity.data.entity.Venues

@Dao
interface VenuesDao {

    @Query("SELECT * FROM venues")
    fun getVenuesAsSingle(): Single<Venues>

    @Query("SELECT * FROM venues")
    fun getVenues(): Venues

    @Query("SELECT COUNT(*) FROM venues")
    fun getNumberOfVenues(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVenue(venueEntity: VenueEntity)

    @Query("SELECT * FROM venue WHERE id = :venueId")
    fun getVenue(venueId: String): Single<VenueEntity>

    @Query("SELECT COUNT(*) FROM venue WHERE id = :venueId")
    fun venueExists(venueId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveVenues(venues: Venues)

    @Query("DELETE FROM venues")
    fun deleteVenues()

}