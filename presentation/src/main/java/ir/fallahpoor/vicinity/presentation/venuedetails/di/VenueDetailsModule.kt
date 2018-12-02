package ir.fallahpoor.vicinity.presentation.venuedetails.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.vicinity.data.mapper.VenuesEntityDataMapper
import ir.fallahpoor.vicinity.data.repository.Database
import ir.fallahpoor.vicinity.data.repository.VenuesRepositoryImpl
import ir.fallahpoor.vicinity.data.repository.dao.VenuesDao
import ir.fallahpoor.vicinity.data.repository.datasource.VenuesDataSourceFactory
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository
import ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel.VenueDetailsViewModelFactory
import ir.fallahpoor.vicinity.presentation.common.ExceptionHandler
import ir.fallahpoor.vicinity.presentation.venuedetails.model.VenueDetailsDataMapper

@Module
class VenueDetailsModule {

    @Provides
    fun provideViewModelFactory(
        getVenuesUseCase: GetVenueDetailsUseCase,
        venueDetailsDataMapper: VenueDetailsDataMapper,
        exceptionHandler: ExceptionHandler
    ): VenueDetailsViewModelFactory {
        return VenueDetailsViewModelFactory(
            getVenuesUseCase,
            venueDetailsDataMapper,
            exceptionHandler
        )
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