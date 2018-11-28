package ir.fallahpoor.vicinity.data.entity

data class VenueDetailsEntity(var response: Response) {
    data class Response(var venue: VenueEntity)
}