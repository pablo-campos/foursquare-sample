package com.pablocampos.foursquaresample.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
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
	private ImageView venueIcon;
	private TextView name;
	private TextView category;
	private TextView distance;
	private ImageView favoriteIcon;



	public VenueViewHolder (View v) {
		super(v);
		cardView = (CardView) v;
		cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.cardBackgroundColor));
		venueIcon = v.findViewById(R.id.venue_icon);
		name = v.findViewById(R.id.venue_name);
		category = v.findViewById(R.id.venue_category);
		distance = v.findViewById(R.id.venue_distance);
		favoriteIcon = v.findViewById(R.id.venue_favorite);
	}



	public void initialize(final Set<String> favorites, final Venue venue, final VenueClickListener venueClickListener){

		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (final View v) {
				venueClickListener.onClick(venue);
			}
		});

		// Name
		name.setText(venue.getName());

		// Categories
		StringBuilder categories = new StringBuilder();
		categories.append(venueIcon.getContext().getResources().getString(R.string.venue_categories_label) + ":");
		for (Category category : venue.getCategories()){
			categories.append(" " + category.getName());
		}
		category.setText(categories);

		// Distance from center of Seattle
		StringBuilder distanceValue = new StringBuilder(
				venueIcon.getContext().getResources().getString(R.string.venue_distance_label) + " " + venue.getLocation().getDistance() + " " + venueIcon
						.getContext().getResources().getString(R.string.venue_distance_meters_label));
		distance.setText(distanceValue);

		// Venue's venueIcon
		if (venue.getCategories().isEmpty()){
			venueIcon.setVisibility(View.GONE);
		} else {
			venueIcon.setVisibility(View.VISIBLE);
			Glide.with(venueIcon.getContext()).load(venue.getCategories().get(0).getIcon().getUrl()).into(venueIcon);
		}

		// Is this a favoriteIcon venue?
		int favoriteIconId, favoriteColor;
		if (favorites.contains(venue.getId())) {
			favoriteIconId = R.drawable.favorite_black;
			favoriteColor = Color.RED;
		} else {
			favoriteIconId = R.drawable.favorite_border_black;
			favoriteColor = Color.LTGRAY;
		}

		favoriteIcon.setImageDrawable(favoriteIcon.getContext().getResources().getDrawable(favoriteIconId, favoriteIcon.getContext().getTheme()));
		favoriteIcon.setColorFilter(favoriteColor, PorterDuff.Mode.SRC_IN);
	}

}