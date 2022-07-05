package com.pablocampos.foursquaresample.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pablocampos.foursquaresample.R;
import com.pablocampos.foursquaresample.models.Response;
import com.pablocampos.foursquaresample.models.Venue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VenueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


	private Set<String> favorites;
	private Response response;
	private List<Venue> venues;
	private VenueClickListener venueClickListener;





	// Provide a suitable constructor (depends on the kind of dataset)
	public VenueAdapter (VenueClickListener venueClickListener) {
		this.favorites = new HashSet<>();
		this.venues = new ArrayList<>();
		this.venueClickListener = venueClickListener;
	}



	// Create new views (invoked by the layout manager)
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_list_item, parent, false);
		VenueViewHolder viewHolder = new VenueViewHolder (view);
		return viewHolder;
	}



	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
		((VenueViewHolder) holder).initialize(favorites, venues.get(position), venueClickListener);
	}



	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount () {
		return venues.size();
	}



	private void updateData(final List<Venue> newData){
		venues.clear();
		venues.addAll(newData);
		notifyDataSetChanged();
	}



	public void updateApiData(final Response response){
		this.response = response;
		if (response == null){
			updateData(new ArrayList<Venue>());
		} else {
			updateData(this.response.getVenues());
		}
	}



	public void updateFavorites(final Set<String> favorites){
		this.favorites = favorites;
		notifyDataSetChanged();
	}



	public Response getResponse (){
		return response;
	}
}