package ir.fallahpoor.vicinity.presentation.venues.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.vicinity.presentation.common.ExceptionHandler
import ir.fallahpoor.vicinity.data.mapper.VenuesEntityDataMapper
import ir.fallahpoor.vicinity.data.repository.Database
import ir.fallahpoor.vicinity.data.repository.VenuesRepositoryImpl
import ir.fallahpoor.vicinity.data.repository.dao.VenuesDao
import ir.fallahpoor.vicinity.data.repository.datasource.VenuesDataSourceFactory
import ir.fallahpoor.vicinity.domain.interactor.GetVenuesUseCase
import ir.fallahpoor.vicinity.domain.repository.VenuesRepository
import ir.fallahpoor.vicinity.presentation.venues.model.VenuesDataMapper
import ir.fallahpoor.vicinity.presentation.venues.presenter.VenuesPresenter
import ir.fallahpoor.vicinity.presentation.venues.presenter.VenuesPresenterImpl

@Module
class VenuesModule {

    @Provides
    internal fun provideVenuesPresenter(
        getVenuesUseCase: GetVenuesUseCase, venuesDataMapper: VenuesDataMapper,
        exceptionHandler: ExceptionHandler
    ): VenuesPresenter {
        return VenuesPresenterImpl(getVenuesUseCase, venuesDataMapper, exceptionHandler)
    }

    @Provides
    internal fun provideVenuesRepository(
        venuesDataSourceFactory: VenuesDataSourceFactory,
        venuesEntityDataMapper: VenuesEntityDataMapper
    ): VenuesRepository {
        return VenuesRepositoryImpl(venuesDataSourceFactory, venuesEntityDataMapper)
    }

    @Provides
    internal fun provideVenuesDao(context: Context): VenuesDao {
        return Database.getDatabase(context).venuesDao()
    }

}