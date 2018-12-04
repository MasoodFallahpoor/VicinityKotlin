package ir.fallahpoor.vicinity.presentation.app

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.vicinity.data.executor.JobExecutor
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread
import ir.fallahpoor.vicinity.domain.executor.ThreadExecutor
import ir.fallahpoor.vicinity.presentation.UiThread

@Module
class AppModule internal constructor(private val context: Context) {

    @Provides
    internal fun provideContext() = context

    @Provides
    internal fun provideThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor = jobExecutor

    @Provides
    internal fun providePostExecutionThread(uiThread: UiThread): PostExecutionThread = uiThread

}