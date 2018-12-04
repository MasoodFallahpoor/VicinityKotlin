package ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionParser
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsViewModel @Inject constructor(
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase,
    private val venueDetailsDataMapper: VenueDetailsDataMapper,
    private val exceptionParser: ExceptionParser
) : ViewModel() {

    val venueDetailsLiveData = MutableLiveData<VenueViewModel>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()
    private var disposable: Disposable? = null

    fun getVenueDetails(venueId: String) {

        if (venueDetailsLiveData.value == null) {
            showProgressLiveData.value = true
            loadVenueDetails(venueId)
        }

    }

    private fun loadVenueDetails(venueId: String) {

        disposable = getVenueDetailsUseCase.execute(GetVenueDetailsUseCase.Inputs.forVenue(venueId))
            .doFinally { showProgressLiveData.value = false }
            .subscribe(
                { venue ->
                    venueDetailsLiveData.value = venueDetailsDataMapper.transform(venue)
                },
                { throwable ->
                    errorLiveData.value = exceptionParser.parseException(throwable)
                })

    }


    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}