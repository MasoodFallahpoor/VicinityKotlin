package ir.fallahpoor.vicinity.domain.interactor.type

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread
import ir.fallahpoor.vicinity.domain.executor.ThreadExecutor

abstract class UseCase<Outputs, Inputs>(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {
    protected abstract fun buildUseCaseObservable(inputs: Inputs): Single<Outputs>

    fun execute(inputs: Inputs): Single<Outputs> {
        return buildUseCaseObservable(inputs)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());
    }

}