package ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionParser
import javax.inject.Inject

class VenueDetailsViewModelFactory @Inject
constructor(
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase,
    private val venueDetailsDataMapper: VenueDetailsDataMapper,
    private val exceptionParser: ExceptionParser
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {

        if (viewModelClass.isAssignableFrom(VenueDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VenueDetailsViewModel(
                getVenueDetailsUseCase,
                venueDetailsDataMapper,
                exceptionParser
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class");
        }

    }

}