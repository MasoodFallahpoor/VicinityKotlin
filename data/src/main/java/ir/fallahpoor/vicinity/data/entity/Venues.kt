package ir.fallahpoor.vicinity.data.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import ir.fallahpoor.vicinity.data.repository.DataConverter

@Entity(tableName = "venues", primaryKeys = ["latitude", "longitude"])
class Venues(var latitude: Double, var longitude: Double, venues: List<VenueEntity>) {

    @TypeConverters(DataConverter::class)
    var venues: List<VenueEntity>? = venues

}