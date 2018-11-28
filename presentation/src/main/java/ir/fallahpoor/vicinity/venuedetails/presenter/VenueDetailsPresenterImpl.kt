package ir.fallahpoor.vicinity.venuedetails.presenter

import ir.fallahpoor.vicinity.venuedetails.model.VenueDetailsDataMapper
import ir.fallahpoor.vicinity.venuedetails.view.VenueDetailsView
import ir.fallahpoor.vicinity.BaseMvpPresenter
import ir.fallahpoor.vicinity.common.ExceptionHandler
import ir.fallahpoor.vicinity.domain.interactor.GetVenueDetailsUseCase

class VenueDetailsPresenterImpl(
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase,
    private val venueDetailsDataMapper: VenueDetailsDataMapper,
    private val exceptionHandler: ExceptionHandler
) : BaseMvpPresenter<VenueDetailsView>(), VenueDetailsPresenter {

    override fun getVenueDetails(venueId: String) {

        ifViewAttached { it.showLoading() }

        val d = getVenueDetailsUseCase.execute(GetVenueDetailsUseCase.Inputs.forVenue(venueId))
            .subscribe(
                { venue ->
                    ifViewAttached { view ->
                        view.hideLoading()
                        view.showVenue(venueDetailsDataMapper.transform(venue))
                    }
                },
                { throwable ->
                    ifViewAttached { view ->
                        val error = exceptionHandler.parseException(throwable)
                        view.hideLoading()
                        view.showError(error.message)
                    }
                })
        addDisposable(d)

    }

}