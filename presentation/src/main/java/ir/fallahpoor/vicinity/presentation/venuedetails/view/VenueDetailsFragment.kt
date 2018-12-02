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
import javax.inject.Inject

class VenueDetailsFragment : Fragment() {

    @Inject
    lateinit var venueDetailsViewModelFactory: VenueDetailsViewModelFactory

    private lateinit var binding: FragmentVenueDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)
    }

    private fun injectDependencies() {
        DaggerVenueDetailsComponent.builder()
            .appComponent((activity?.application as App).getAppComponent())
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        binding = FragmentVenueDetailsBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        val venueDetailsViewModel = ViewModelProviders.of(activity!!, venueDetailsViewModelFactory)
            .get(VenueDetailsViewModel::class.java)
        setupSubscriptions(venueDetailsViewModel)

        venueDetailsViewModel.getVenueDetails(getVenueId())

    }

    private fun setupSubscriptions(venueDetailsViewModel: VenueDetailsViewModel) {

        venueDetailsViewModel.venueLiveData.observe(
            activity!!,
            Observer { venueViewModel ->
                binding.venueViewModel = venueViewModel
                binding.contentLayout.visibility = View.VISIBLE
                binding.tryAgain.tryAgainLayout.visibility = View.GONE
            })

        venueDetailsViewModel.showProgressLiveData.observe(
            activity!!,
            Observer { isVisible ->
                if (isVisible) {
                    binding.tryAgain.tryAgainLayout.visibility = View.GONE
                    binding.loading.loadingLayout.visibility = View.VISIBLE
                } else {
                    binding.loading.loadingLayout.visibility = View.GONE
                }
            })

        venueDetailsViewModel.errorLiveData.observe(
            activity!!,
            Observer { errorMessage ->
                binding.tryAgain.errorMessageTextView.text = errorMessage
                binding.tryAgain.tryAgainButton.setOnClickListener {
                    venueDetailsViewModel.getVenueDetails(getVenueId())
                }
                binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
                binding.contentLayout.visibility = View.GONE
            }
        )

    }

    private fun getVenueId(): String {
        arguments?.let {
            return VenueDetailsFragmentArgs.fromBundle(it).venueId
        }
    }

}
