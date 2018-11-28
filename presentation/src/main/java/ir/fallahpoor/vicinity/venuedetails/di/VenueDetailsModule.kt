package ir.fallahpoor.vicinity.venuedetails.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.vicinity.venuedetails.model.VenueDetailsDataMapper
import ir.fallahpoor.vicinity.venuedetails.presenter.VenueDetailsPresenter
import ir.fallahpoor.vicinity.venuedetails.presenter.VenueDetailsPresenterImpl
import ir.fallahpoor.vicinity.common.ExceptionHandler
import ir.fallahpoor.vicinity.data.mapper.VenuesEntityDataMapper
import ir.fallahpoor.vicinity.data.repository.Database
import ir.fallahpoor.vicinity.data.repository.VenuesRepositoryImpl
import ir.fallahpoor.vicinity.data.repository.dao.VenuesDao
import ir.fallahpoor.vicinity.data.repository.datasource.VenuesDataSourceFactory
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository

@Module
class VenueDetailsModule {

    @Provides
    fun provideVenueDetailsPresenter(
        getVenuesUseCase: GetVenueDetailsUseCase,
        venueDetailsDataMapper: VenueDetailsDataMapper,
        exceptionHandler: ExceptionHandler
    ): VenueDetailsPresenter {
        return VenueDetailsPresenterImpl(getVenuesUseCase, venueDetailsDataMapper, exceptionHandler)
    }

    @Provides
    fun provideVenuesRepository(
        venuesDataSourceFactory: VenuesDataSourceFactory,
        venuesEntityDataMapper: VenuesEntityDataMapper
    ): VenuesRepository {
        return VenuesRepositoryImpl(venuesDataSourceFactory, venuesEntityDataMapper)
    }

    @Provides
    fun provideVenuesDao(context: Context): VenuesDao {
        return Database.getDatabase(context).venuesDao()
    }

}