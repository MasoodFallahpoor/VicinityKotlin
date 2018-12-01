package ir.fallahpoor.vicinity.presentation.app

import android.content.Context
import dagger.Component
import ir.fallahpoor.vicinity.domain.executor.PostExecutionThread
import ir.fallahpoor.vicinity.domain.executor.ThreadExecutor

@Component(modules = [AppModule::class])
interface AppComponent {
    fun context(): Context

    fun threadExecutor(): ThreadExecutor

    fun postExecutionThread(): PostExecutionThread
}