package com.pablocampos.foursquaresample.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pablocampos.foursquaresample.R;
import com.pablocampos.foursquaresample.models.Venue;

import java.util.HashSet;
import java.util.Set;

import static com.pablocampos.foursquaresample.activities.SearchActivity.SEATTLE_FAVORITE_VENUES;
import static com.pablocampos.foursquaresample.activities.SearchActivity.SEATTLE_SEARCH_PREFERENCES;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {


	private Venue venue;
	private FloatingActionButton fab;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_details);

		// Retrieve set of favorite places:
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.details_map);
		mapFragment.getMapAsync(this);

		// For this case, we are going to use a single TextView, but this could be a lot more customizable
		venue = (Venue) getIntent().getSerializableExtra(SearchActivity.SELECTED_VENUE);
		TextView descriptionTextView = findViewById(R.id.description);
		descriptionTextView.setText(getFormattedDescription());

		// Initialize this venue's website
		TextView venueWebsite = findViewById(R.id.venue_website);
		SpannableString content = new SpannableString(getResources().getString(R.string.venue_website_label));
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		venueWebsite.setText(content);
		venueWebsite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (final View v) {
				String urlToVisit = venue.getUrl() == null ? getResources().getString(R.string.default_venue_website) : venue.getUrl();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToVisit));
				startActivity(browserIntent);
			}
		});

		// Initialize and update favorite button
		fab = findViewById(R.id.fab);
		updateFabIcon();
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {

				// Let's add/remove this venue from the user's favorite places
				if (toggleFavorites()){
					Snackbar.make(view, getResources().getString(R.string.venue_added_favorite), Snackbar.LENGTH_LONG).show();
				} else {
					Snackbar.make(view, getResources().getString(R.string.venue_removed_favorite), Snackbar.LENGTH_LONG).show();
				}
				updateFabIcon();
			}
		});
	}



	/**
	 * Toggle the current "favorite" status of this venue and
	 * update user settings.
	 * @return whether this venue was added or removed from favorites.
	 */
	private boolean toggleFavorites(){
		boolean added = false;
		final Set<String> favoritePlaces = getSharedPreferences(SEATTLE_SEARCH_PREFERENCES, MODE_MULTI_PROCESS).getStringSet(SEATTLE_FAVORITE_VENUES, new HashSet<String>());
		final Set<String> updatedFavoritePlaces = new HashSet<>();		// You shoul not attempt to modify the set return from shared prefs but rather create a new one
		updatedFavoritePlaces.addAll(favoritePlaces);
		if (updatedFavoritePlaces.contains(venue.getId())){
			updatedFavoritePlaces.remove(venue.getId());
		} else {
			updatedFavoritePlaces.add(venue.getId());
			added = true;
		}
		getSharedPreferences(SEATTLE_SEARCH_PREFERENCES, MODE_MULTI_PROCESS).edit().putStringSet(SEATTLE_FAVORITE_VENUES, updatedFavoritePlaces).apply();	// Update prefs
		return added;
	}



	/**
	 * Update the icon of the favorite button depending on whether
	 * ot not this venue has been saved already.
	 */
	private void updateFabIcon(){
		final Set<String> favoritePlaces = getSharedPreferences(SEATTLE_SEARCH_PREFERENCES, MODE_MULTI_PROCESS).getStringSet(SEATTLE_FAVORITE_VENUES, new HashSet<String>());
		int favoriteIconId = favoritePlaces.contains(venue.getId()) ? R.drawable.favorite_white_icon : R.drawable.favorite_border_white_icon;
		fab.setImageDrawable(getResources().getDrawable(favoriteIconId, getTheme()));
	}



	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady (final GoogleMap googleMap) {

		// Removing map controls since we already have a full screen map
		googleMap.getUiSettings().setAllGesturesEnabled(false);

		// Move camera to center of Seattle
		float seattleCenterLat = Float.valueOf(getResources().getString(R.string.seattle_center_lat));
		float seattleCenterLng = Float.valueOf(getResources().getString(R.string.seattle_center_lng));
		LatLng seattleCenter = new LatLng(seattleCenterLat, seattleCenterLng);
		googleMap.addMarker(new MarkerOptions().position(seattleCenter).title("Center of Seattle"));

		// If we have results (venues), let's display them on the map
		LatLngBounds.Builder builder = new LatLngBounds.Builder();

		// Create a marker for the selected venue
		LatLng venueLocation = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
		Marker venueMarker = googleMap.addMarker(new MarkerOptions().position(venueLocation).title(venue.getName()));
		venueMarker.setTag(venue);
		builder.include(venueLocation);

		// Let's add the center of Seattle
		builder.include(seattleCenter);

		// Let's update zoom to fit both markers
		LatLngBounds bounds = builder.build();
		int padding = 250; 	// offset from edges of the map in pixels
		final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

		googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
			@Override
			public void onMapLoaded () {
				googleMap.moveCamera(cu);
			}
		});
	}



	/**
	 * Format a long string with newlines as we desire given certain
	 * details of this venue.
	 * @return formatted text.
	 */
	private String getFormattedDescription(){
		StringBuilder formattedDescription = new StringBuilder();
		formattedDescription.append(venue.getName()).append("\n\n");
		formattedDescription.append(venue.getCategories().get(0).getName()).append("\n\n");
		formattedDescription.append(venue.getLocation().getFormattedAddress()).append("\n");
		formattedDescription.append("We can continue adding more details here...");
		return formattedDescription.toString();
	}
}
