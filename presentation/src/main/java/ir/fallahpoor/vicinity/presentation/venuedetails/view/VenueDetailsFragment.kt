package ir.fallahpoor.vicinity.presentation.venuedetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ir.fallahpoor.vicinity.databinding.FragmentVenueDetailsBinding
import ir.fallahpoor.vicinity.presentation.app.App
import ir.fallahpoor.vicinity.presentation.venuedetails.di.DaggerVenueDetailsComponent
import ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel.VenueDetailsViewModel
import ir.fallahpoor.vicinity.presentation.venuedetails.viewmodel.VenueDetailsViewModelFactory
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsFragment : Fragment() {

    @Inject
    lateinit var venueDetailsViewModelFactory: VenueDetailsViewModelFactory

    private lateinit var binding: FragmentVenueDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        binding = FragmentVenueDetailsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        injectDependencies()

        super.onActivityCreated(savedInstanceState)

        val venueDetailsViewModel = ViewModelProviders.of(this, venueDetailsViewModelFactory)
            .get(VenueDetailsViewModel::class.java)

        subscribeToViewModel(venueDetailsViewModel)

        venueDetailsViewModel.getVenueDetails(getVenueId())

    }

    private fun injectDependencies() {
        DaggerVenueDetailsComponent.builder()
            .appComponent((activity?.application as App).appComponent)
            .build()
            .inject(this)
    }

    private fun subscribeToViewModel(venueDetailsViewModel: VenueDetailsViewModel) {

        venueDetailsViewModel.venueDetailsLiveData.observe(
            this,
            Observer { venueViewModel -> showVenueDetails(venueViewModel) })

        venueDetailsViewModel.showProgressLiveData.observe(
            this,
            Observer { show -> if (show) showLoading() else hideLoading() })

        venueDetailsViewModel.errorLiveData.observe(
            this,
            Observer { errorMessage -> showError(errorMessage, venueDetailsViewModel) }
        )

    }

    private fun getVenueId(): String {
        arguments?.let {
            return VenueDetailsFragmentArgs.fromBundle(it).venueId
        }
    }

    private fun showVenueDetails(venueViewModel: VenueViewModel) {
        binding.venueViewModel = venueViewModel
        binding.contentLayout.visibility = View.VISIBLE
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
    }

    private fun showLoading() {
        binding.tryAgain.tryAgainLayout.visibility = View.GONE
        binding.loading.loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE
    }

    private fun showError(errorMessage: String, venueDetailsViewModel: VenueDetailsViewModel) {
        binding.tryAgain.errorMessageTextView.text = errorMessage
        binding.tryAgain.tryAgainButton.setOnClickListener {
            venueDetailsViewModel.getVenueDetails(getVenueId())
        }
        binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
    }

}
