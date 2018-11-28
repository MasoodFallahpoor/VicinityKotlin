package ir.fallahpoor.vicinity.app

import android.app.Application

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {

        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

}