package ir.fallahpoor.vicinity.venues.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import ir.fallahpoor.vicinity.venuedetails.view.VenueDetailsActivity
import ir.fallahpoor.vicinity.R
import ir.fallahpoor.vicinity.venues.model.VenueViewModel

class VenuesAdapter internal constructor(
    private val context: Context,
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
            val intent = Intent(context, VenueDetailsActivity::class.java).apply {
                putExtra(VenueDetailsActivity.KEY_VENUE_ID, id)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return places.size
    }

    inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var venueNameTextView: TextView = itemView.findViewById(R.id.venue_name_text_view)
    }

}