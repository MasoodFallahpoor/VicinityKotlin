package ir.fallahpoor.vicinity.data.entity

data class VenuesEntity(var response: Response) {
    data class Response(var venues: List<VenueEntity>)
}