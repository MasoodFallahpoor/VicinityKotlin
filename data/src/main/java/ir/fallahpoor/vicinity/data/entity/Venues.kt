package ir.fallahpoor.vicinity.data.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import ir.fallahpoor.vicinity.data.repository.DataConverter

@Entity(tableName = "venues", primaryKeys = ["latitude", "longitude"])
class Venues {

    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()
    @TypeConverters(DataConverter::class)
    var venues: List<VenueEntity>? = null

    constructor()

    constructor(latitude: Double, longitude: Double, venues: List<VenueEntity>) {
        this.latitude = latitude
        this.longitude = longitude
        this.venues = venues
    }

}