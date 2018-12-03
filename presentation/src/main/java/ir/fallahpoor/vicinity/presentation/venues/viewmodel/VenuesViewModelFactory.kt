package ir.fallahpoor.vicinity.presentation.venues.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.fallahpoor.vicinity.domain.interactor.GetVenuesUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionHandler
import ir.fallahpoor.vicinity.presentation.venues.model.VenuesDataMapper
import javax.inject.Inject

class VenuesViewModelFactory @Inject
constructor(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val venuesDataMapper: VenuesDataMapper,
    private val exceptionHandler: ExceptionHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {

        if (viewModelClass.isAssignableFrom(VenuesViewModel::class.java)) {
            return VenuesViewModel(
                getVenuesUseCase,
                venuesDataMapper,
                exceptionHandler
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class");
        }

    }

}