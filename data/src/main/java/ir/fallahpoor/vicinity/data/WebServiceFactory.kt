package ir.fallahpoor.vicinity.data

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WebServiceFactory @Inject
constructor() {

    private val retrofitBuilder: Retrofit.Builder
        get() = Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/v2/venues/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))

    fun <S> createService(serviceClass: Class<S>): S {
        val builder = retrofitBuilder
        val retrofit = builder.build()
        return retrofit.create(serviceClass)
    }

}