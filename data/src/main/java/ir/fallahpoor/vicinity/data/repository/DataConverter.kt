package ir.fallahpoor.vicinity.data.repository

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.fallahpoor.vicinity.data.entity.VenueEntity

class DataConverter {

    @TypeConverter
    fun fromVenueEntityList(venueEntities: List<VenueEntity>?): String? {

        if (venueEntities == null) {
            return null
        }

        val type = object : TypeToken<List<VenueEntity>>() {}.type
        return Gson().toJson(venueEntities, type)

    }

    @TypeConverter
    fun toVenueEntityList(venueEntitiesString: String?): List<VenueEntity>? {

        if (venueEntitiesString == null) {
            return null
        }

        val type = object : TypeToken<List<VenueEntity>>() {}.type
        return Gson().fromJson(venueEntitiesString, type)

    }

}