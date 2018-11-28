package ir.fallahpoor.vicinity.data.entity

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey
    @NonNull
    var id: String,
    var name: String,
    @Embedded
    var location: LocationEntity
)