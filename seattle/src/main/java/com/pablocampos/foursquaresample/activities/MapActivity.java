package com.pablocampos.foursquaresample.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

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
import com.pablocampos.foursquaresample.models.Response;
import com.pablocampos.foursquaresample.models.Venue;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
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

		// Move camera to center of Seattle
		final float seattleCenterLat = Float.valueOf(getResources().getString(R.string.seattle_center_lat));
		final float seattleCenterLng = Float.valueOf(getResources().getString(R.string.seattle_center_lng));
		final LatLng seattleCenter = new LatLng(seattleCenterLat, seattleCenterLng);

		// If we have results (venues), let's display them on the map
		if (getIntent().hasExtra(SearchActivity.CURRENT_RESPONSE) && getIntent().getSerializableExtra(SearchActivity.CURRENT_RESPONSE) != null){
			Response response = (Response) getIntent().getSerializableExtra(SearchActivity.CURRENT_RESPONSE);

			final LatLngBounds.Builder builder = new LatLngBounds.Builder();

			// Create a marker for each venue
			for (Venue venue : response.getVenues()){
				final LatLng venueLocation = new LatLng(venue.getLocation().getLat(), venue.getLocation().getLng());
				final Marker venueMarker = googleMap.addMarker(new MarkerOptions().position(venueLocation).title(venue.getName()));
				venueMarker.setTag(venue);
				builder.include(venueLocation);
			}

			// Let's update zoom to fit all markers
			final LatLngBounds bounds = builder.build();
			final int padding = 200; 	// offset from edges of the map in pixels
			final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

			googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
				@Override
				public void onMapLoaded () {
					googleMap.animateCamera(cu);
				}
			});

		} else {

			// Since we have no venues to display, let's go ahead and clear the map and adjust zoom
			googleMap.clear();
			googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
				@Override
				public void onMapLoaded () {
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(seattleCenter, 13));
				}
			});
		}

		googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {
				Venue venue = (Venue) marker.getTag();

				// Open details activity
				Intent detailsActivity = new Intent(MapActivity.this, DetailsActivity.class);
				detailsActivity.putExtra(SearchActivity.SELECTED_VENUE, venue);
				startActivity(detailsActivity, null);
			}
		});
	}
}
