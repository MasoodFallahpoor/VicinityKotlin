package ir.fallahpoor.vicinity.data.entity

import com.google.gson.annotations.SerializedName

data class LocationEntity(
    var address: String?,
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lng")
    var longitude: Double
)