package ir.fallahpoor.vicinity.presentation.venues.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.vicinity.data.mapper.VenuesEntityDataMapper
import ir.fallahpoor.vicinity.data.repository.Database
import ir.fallahpoor.vicinity.data.repository.VenuesRepositoryImpl
import ir.fallahpoor.vicinity.data.repository.datasource.VenuesDataSourceFactory
import ir.fallahpoor.vicinity.domain.interactor.GetVenuesUseCase
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository
import ir.fallahpoor.vicinity.presentation.common.ExceptionParser
import ir.fallahpoor.vicinity.presentation.venues.model.VenuesDataMapper
import ir.fallahpoor.vicinity.presentation.venues.viewmodel.VenuesViewModelFactory

@Module
class VenuesModule {

    @Provides
    internal fun provideVenuesViewModelFactory(
        getVenuesUseCase: GetVenuesUseCase, venuesDataMapper: VenuesDataMapper,
        exceptionParser: ExceptionParser
    ) = VenuesViewModelFactory(getVenuesUseCase, venuesDataMapper, exceptionParser)


    @Provides
    internal fun provideVenuesRepository(
        venuesDataSourceFactory: VenuesDataSourceFactory,
        venuesEntityDataMapper: VenuesEntityDataMapper
    ) : VenuesRepository = VenuesRepositoryImpl(venuesDataSourceFactory, venuesEntityDataMapper)

    @Provides
    internal fun provideVenuesDao(context: Context) = Database.getDatabase(context).venuesDao()

}