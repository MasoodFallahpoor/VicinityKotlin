package ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase
import ir.fallahpoor.vicinity.presentation.common.ExceptionHandler
import ir.fallahpoor.vicinity.presentation.venuedetails.model.VenueDetailsDataMapper
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsViewModel @Inject constructor(
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase,
    private val venueDetailsDataMapper: VenueDetailsDataMapper,
    private val exceptionHandler: ExceptionHandler
) : ViewModel() {

    val venueLiveData = MutableLiveData<VenueViewModel>()
    val showProgressLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()
    private var disposable: Disposable? = null

    fun getVenueDetails(venueId: String) {

        showProgressLiveData.value = true

        disposable = getVenueDetailsUseCase.execute(GetVenueDetailsUseCase.Inputs.forVenue(venueId))
            .subscribe(
                { venue ->
                    showProgressLiveData.value = false
                    venueLiveData.postValue(venueDetailsDataMapper.transform(venue))
                },
                { throwable ->
                    showProgressLiveData.value = false
                    errorLiveData.postValue(exceptionHandler.parseException(throwable).message)
                })

    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

}