package ir.fallahpoor.vicinity.venuedetails.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import ir.fallahpoor.vicinity.venuedetails.di.DaggerVenueDetailsComponent
import ir.fallahpoor.vicinity.venuedetails.presenter.VenueDetailsPresenter
import ir.fallahpoor.vicinity.R
import ir.fallahpoor.vicinity.app.App
import ir.fallahpoor.vicinity.databinding.ActivityVenueDetailsBinding
import ir.fallahpoor.vicinity.venues.model.VenueViewModel
import javax.inject.Inject

class VenueDetailsActivity : MvpActivity<VenueDetailsView, VenueDetailsPresenter>(),
    VenueDetailsView {

    companion object {
        const val KEY_VENUE_ID = "venue_id"
    }

    @Inject
    lateinit var venueDetailsPresenter: VenueDetailsPresenter

    private lateinit var binding: ActivityVenueDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        injectDependencies()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_venue_details)

        setupActionBar()

        getPresenter().getVenueDetails(getVenueId())

    }

    private fun injectDependencies() {
        DaggerVenueDetailsComponent.builder()
            .appComponent((application as App).getAppComponent())
            .build()
            .inject(this)
    }

    private fun getVenueId(): String {
        val bundle = intent.extras
        return if (bundle == null) {
            ""
        } else {
            bundle.getString(KEY_VENUE_ID) ?: ""
        }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun createPresenter(): VenueDetailsPresenter {
        return venueDetailsPresenter
    }

    override fun showLoading() {
        binding.tryAgain.tryAgainLayout.visibility = View.GONE;
        binding.loading.loadingLayout.visibility = View.VISIBLE;
    }

    override fun hideLoading() {
        binding.loading.loadingLayout.visibility = View.GONE;
    }

    override fun showError(errorMessage: String) {
        binding.tryAgain.errorMessageTextView.text = errorMessage
        binding.tryAgain.tryAgainButton.setOnClickListener {
            getPresenter().getVenueDetails(
                getVenueId()
            )
        }
        binding.tryAgain.tryAgainLayout.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
    }

    override fun showVenue(venue: VenueViewModel) {
        title = venue.name

        binding.venueViewModel = venue;

        binding.contentLayout.visibility = View.VISIBLE;
        binding.tryAgain.tryAgainLayout.visibility = View.GONE;
    }

}
