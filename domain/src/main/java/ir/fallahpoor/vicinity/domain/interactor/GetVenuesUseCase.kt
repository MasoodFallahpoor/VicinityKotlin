package ir.fallahpoor.vicinity.domain.interactor

import io.reactivex.Single
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread
import ir.fallahpoor.vicinity.domain.executor.ThreadExecutor
import ir.fallahpoor.vicinity.domain.interactor.type.UseCase
import ir.fallahpoor.vicinity.domain.model.Venue
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository
import javax.inject.Inject

class GetVenuesUseCase @Inject constructor(
    private val venuesRepository: VenuesRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<List<Venue>, GetVenuesUseCase.Inputs>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(inputs: Inputs): Single<List<Venue>> {
        return venuesRepository.getVenuesAround(inputs.latitude, inputs.longitude)
    }

    class Inputs private constructor(val latitude: Double, val longitude: Double) {
        companion object {
            fun forLocation(latitude: Double, longitude: Double): Inputs {
                return Inputs(latitude, longitude)
            }
        }
    }

}