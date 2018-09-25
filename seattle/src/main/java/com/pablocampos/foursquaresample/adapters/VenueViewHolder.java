package com.pablocampos.foursquaresample.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pablocampos.foursquaresample.R;
import com.pablocampos.foursquaresample.models.Category;
import com.pablocampos.foursquaresample.models.Venue;

import java.util.Set;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class VenueViewHolder extends RecyclerView.ViewHolder {

	// Each data item is just a string in this case
	private CardView cardView;
	private ImageView icon;
	private TextView name;
	private TextView category;
	private TextView distance;
	private ImageView favorite;



	public VenueViewHolder (View v) {
		super(v);
		cardView = (CardView) v;
		cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.cardBackgroundColor));
		icon = v.findViewById(R.id.venue_icon);
		name = v.findViewById(R.id.venue_name);
		category = v.findViewById(R.id.venue_category);
		distance = v.findViewById(R.id.venue_distance);
		favorite = v.findViewById(R.id.venue_favorite);
	}



	public void initialize(final Set<String> favorites, final Venue venue, final VenueClickListener oeuvreClickListener){

		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (final View v) {
				oeuvreClickListener.onClick(venue);
			}
		});

		// Name
		name.setText(venue.getName());

		// Categories
		StringBuilder categories = new StringBuilder();
		categories.append(icon.getContext().getResources().getString(R.string.venue_categories_label) + ":");
		for (Category category : venue.getCategories()){
			categories.append(" " + category.getName());
		}
		category.setText(categories);

		// Distance from center of Seattle
		StringBuilder distanceValue = new StringBuilder(icon.getContext().getResources().getString(R.string.venue_distance_label) + " " + venue.getLocation().getDistance() + " " + icon.getContext().getResources().getString(R.string.venue_distance_meters_label));
		distance.setText(distanceValue);

		// Venue's icon
		if (venue.getCategories().isEmpty()){
			icon.setVisibility(View.GONE);
		} else {
			icon.setVisibility(View.VISIBLE);
			Glide.with(icon.getContext()).load(venue.getCategories().get(0).getIcon().getUrl()).into(icon);
		}

		// Is this a favorite venue?
		int favoriteIconId = favorites.contains(venue.getId()) ? R.drawable.favorite_black : R.drawable.favorite_border_black;
		favorite.setImageDrawable(favorite.getContext().getResources().getDrawable(favoriteIconId, favorite.getContext().getTheme()));
	}

}