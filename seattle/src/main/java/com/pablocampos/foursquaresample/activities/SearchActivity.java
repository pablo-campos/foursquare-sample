package com.pablocampos.foursquaresample.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pablocampos.foursquaresample.R;
import com.pablocampos.foursquaresample.adapters.VenueAdapter;
import com.pablocampos.foursquaresample.adapters.VenueClickListener;
import com.pablocampos.foursquaresample.models.ApiData;
import com.pablocampos.foursquaresample.models.Venue;
import com.pablocampos.foursquaresample.network.RetrofitFoursquareApi;
import com.pablocampos.foursquaresample.network.RetrofitFoursquareInterface;
import com.pablocampos.foursquaresample.utils.NetworkUtils;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


	public static final String CURRENT_RESPONSE = "CURRENT_RESPONSE";
	public static final String SELECTED_VENUE = "SELECTED_VENUE";

	public static final String SEATTLE_SEARCH_PREFERENCES = "SEATTLE_SEARCH_APP_PREFERENCES";
	public static final String SEATTLE_FAVORITE_VENUES = "SEATTLE_FAVORITE_VENUES_PREFERENCE";

	private final String CURRENT_SEARCH = "CURRENT_SEARCH";		// Current query

	// Current query
	private String currentSearchQuery;
	private Call<ApiData> apiCall;

	// Views
	private SearchView searchView;
	private VenueAdapter venueAdapter;
	private RecyclerView recyclerView;



	@Override
	public boolean onCreateOptionsMenu( Menu menu) {

		getMenuInflater().inflate(R.menu.menu_search, menu);

		// Initialize the search functionality on the activity's toolbar
		final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) myActionMenuItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				if(!searchView.isIconified()) {
					searchView.setIconified(true);
				}
				myActionMenuItem.collapseActionView();
				return false;
			}
			@Override
			public boolean onQueryTextChange(String query) {

				// If search view is empty, let's update the adapter with zero items, if not, let's request a new search query:
				if (query.isEmpty()){
					venueAdapter.updateApiData(null);
				} else if (NetworkUtils.checkInternet(SearchActivity.this)){		// Check internet connection and then perform query

					// Cancel any pending queries
					if (apiCall != null){
						apiCall.cancel();
					}

					performQuery(query);
				}

				return false;
			}
		});

		if (currentSearchQuery != null && !currentSearchQuery.isEmpty()){
			MenuItemCompat.expandActionView(myActionMenuItem);		// We need to expand the search view before
			searchView.setQuery(currentSearchQuery, false);
		}

		return true;
	}



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Initialize toolbar
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		VenueClickListener venueClickListener = new VenueClickListener() {
			@Override
			public void onClick (final Venue venue) {

				// Open details activity
				Intent detailsActivity = new Intent(SearchActivity.this, DetailsActivity.class);
				detailsActivity.putExtra(SELECTED_VENUE, venue);
				startActivity(detailsActivity, null);
			}
		};

		// Initialize RecyclerView with a new adapter
		recyclerView = findViewById(R.id.venues_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		venueAdapter = new VenueAdapter(venueClickListener);
		recyclerView.setAdapter(venueAdapter);

		// Initialize FAB
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
				Intent mapActivity = new Intent(SearchActivity.this, MapActivity.class);
				mapActivity.putExtra(CURRENT_RESPONSE, venueAdapter.getResponse());
				startActivity(mapActivity, null);
			}
		});
	}



	@Override
	protected void onRestoreInstanceState (final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Let's restore the activity's state before it was destroyed
		if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SEARCH)){
			currentSearchQuery = savedInstanceState.getString(CURRENT_SEARCH);
		}
	}



	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current search query
		outState.putString(CURRENT_SEARCH, searchView.getQuery().toString());
	}



	@Override
	protected void onResume () {
		super.onResume();

		// Let's make sure we update the adapter's favorite list in case user updated
		// come places (added/removed) in the details screen
		final Set<String> favoritePlaces = getSharedPreferences(SEATTLE_SEARCH_PREFERENCES, MODE_MULTI_PROCESS).getStringSet(SearchActivity.SEATTLE_FAVORITE_VENUES, new HashSet<String>());
		venueAdapter.updateFavorites(favoritePlaces);
	}



	private void performQuery(final String query){

		String clientId = getResources().getString(R.string.foursquare_client_id);
		String clientSecret = getResources().getString(R.string.foursquare_client_secret);
		String near = getResources().getString(R.string.venues_near);
		String v = getResources().getString(R.string.venues_v);
		String limit = getResources().getString(R.string.venues_limit);
		String seattleCenterLat = getResources().getString(R.string.seattle_center_lat);
		String seattleCenterLng = getResources().getString(R.string.seattle_center_lng);
		String seattleCenter = seattleCenterLat + "," + seattleCenterLng;

		RetrofitFoursquareInterface service = RetrofitFoursquareApi.getClient().create(RetrofitFoursquareInterface.class);

		apiCall = service.getVenues(clientId, clientSecret, near, query, v, limit, seattleCenter);
		apiCall.enqueue(new Callback<ApiData>() {
			@Override
			public void onResponse(Call<ApiData> call, Response<ApiData> response) {
				venueAdapter.updateApiData(response.body().getResponse());		// Update data on adapter
			}

			@Override
			public void onFailure(Call<ApiData> call, Throwable t) {

				// Display an error
				Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
			}
		});
	}

}
