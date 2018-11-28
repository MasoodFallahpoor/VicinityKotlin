package ir.fallahpoor.vicinity

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread

class UiThread @Inject
constructor() : PostExecutionThread {

    override fun getScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}