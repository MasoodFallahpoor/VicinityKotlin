package ir.fallahpoor.vicinity.presentation.venues.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ir.fallahpoor.vicinity.R
import ir.fallahpoor.vicinity.presentation.venuedetails.view.VenueDetailsFragment
import ir.fallahpoor.vicinity.presentation.venues.model.VenueViewModel

class VenuesAdapter internal constructor(
    context: Context,
    private val places: List<VenueViewModel>
) : RecyclerView.Adapter<VenuesAdapter.VenueViewHolder>() {

    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = layoutInflater.inflate(R.layout.recycler_item_venue, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: VenueViewHolder, position: Int) {

        val (id, name) = places[position]

        holder.venueNameTextView.text = name
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString(VenueDetailsFragment.KEY_VENUE_ID, id)
            }
            it.findNavController()
                .navigate(R.id.action_venuesFragment_to_venueDetailsFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return places.size
    }

    inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var venueNameTextView: TextView = itemView.findViewById(R.id.venue_name_text_view)
    }

}