package ir.fallahpoor.vicinity.domain.interactor

import io.reactivex.Single
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread
import ir.fallahpoor.vicinity.domain.executor.ThreadExecutor
import ir.fallahpoor.vicinity.domain.interactor.type.UseCase
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository
import javax.inject.Inject

class GetVenueDetailsUseCase @Inject constructor(
    private val venuesRepository: VenuesRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<Venue, GetVenueDetailsUseCase.Inputs>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(inputs: Inputs): Single<Venue> {
        return venuesRepository.getVenueDetails(inputs.venueId)
    }

    class Inputs private constructor(val venueId: String) {
        companion object {
            fun forVenue(venueId: String): Inputs {
                return Inputs(venueId)
            }
        }
    }

}