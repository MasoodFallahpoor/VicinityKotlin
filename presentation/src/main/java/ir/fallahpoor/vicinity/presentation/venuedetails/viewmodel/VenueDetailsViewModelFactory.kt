package ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionHandler
import ir.fallahpoor.vicinity.presentation.venuedetails.model.VenueDetailsDataMapper
import javax.inject.Inject

class VenueDetailsViewModelFactory @Inject
constructor(
    private val getVenuesUseCase: GetVenueDetailsUseCase,
    private val venueDetailsDataMapper: VenueDetailsDataMapper,
    private val exceptionHandler: ExceptionHandler
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {

        if (viewModelClass.isAssignableFrom(VenueDetailsViewModel::class.java)) {
            return VenueDetailsViewModel(
                getVenuesUseCase,
                venueDetailsDataMapper,
                exceptionHandler
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class");
        }

    }

}