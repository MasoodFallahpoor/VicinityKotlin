package ir.fallahpoor.vicinity.presentation.venuedetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import ir.fallahpoor.vicinity.databinding.FragmentVenueDetailsBinding
import ir.fallahpoor.vicinity.presentation.app.App
import ir.fallahpoor.vicinity.presentation.venuedetails.di.DaggerVenueDetailsComponent
import ir.fallahpoor.vicinity.presentation.venuedetails.presenter.VenueDetailsPresenter
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsFragment : MvpFragment<VenueDetailsView, VenueDetailsPresenter>(),
    VenueDetailsView {

    @Inject
    lateinit var venueDetailsPresenter: VenueDetailsPresenter

    private lateinit var binding: FragmentVenueDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        binding = FragmentVenueDetailsBinding.inflate(inflater, container, false)

        getPresenter().getVenueDetails(getVenueId())

        return binding.root

    }

    private fun injectDependencies() {
        DaggerVenueDetailsComponent.builder()
            .appComponent((activity?.application as App).getAppComponent())
            .build()
            .inject(this)
    }

    private fun getVenueId(): String {
        arguments?.let {
            return VenueDetailsFragmentArgs.fromBundle(it).venueId
        }
    }

    override fun createPresenter(): VenueDetailsPresenter {
        return venueDetailsPresenter
    }

    override fun showLoading() {
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.loading.loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE
    }

    override fun showError(errorMessage: String) {
        binding.tryAgain.errorMessageTextView.text = errorMessage
        binding.tryAgain.tryAgainButton.setOnClickListener {
            getPresenter().getVenueDetails(getVenueId())
        }
        binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
    }

    override fun showVenue(venue: VenueViewModel) {
        binding.venueViewModel = venue
        binding.contentLayout.visibility = View.VISIBLE
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
    }

}
