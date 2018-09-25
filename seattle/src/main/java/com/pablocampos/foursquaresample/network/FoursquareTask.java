package com.pablocampos.foursquaresample.network;

import android.os.AsyncTask;

import com.pablocampos.foursquaresample.adapters.VenueAdapter;
import com.pablocampos.foursquaresample.models.ApiData;

import java.net.MalformedURLException;
import java.net.URL;

public class FoursquareTask extends AsyncTask {



	final String FORMATTED_URL = "https://api.foursquare.com/v2/venues/search?client_id=%s&client_secret=%s&near=%s&query=%s&v=%s&limit=%s&ll=%s";

	VenueAdapter venueAdapter;

	String query;
	String clientId;
	String clientSecret;
	String near;
	String v;
	String limit;
	String seattleCenter;



	public FoursquareTask (
			final VenueAdapter venueAdapter, final String query, final String clientId, final String clientSecret, final String near, final String v, final String limit,
			final String seattleCenter)
	{
		this.venueAdapter = venueAdapter;
		this.query = query;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.near = near;
		this.v = v;
		this.limit = limit;
		this.seattleCenter = seattleCenter;
	}



	@Override
	protected void onPreExecute() {
		// we can start a progress bar here
	}



	@Override
	protected Object doInBackground (final Object[] objects) {

		URL url = null;
		try {

			url = new URL(String.format(FORMATTED_URL, clientId, clientSecret, near, query, v, limit, seattleCenter));

		} catch (MalformedURLException e){

			// The URL should work just fine since it has been tested accordingly,
			// but if there was a chance of error, we could return a custom error
			// message here and return to UI.
		}

		return FoursquareAPI.getData(url);
	}



	@Override
	protected void onPostExecute (final Object o) {

		if (o == null) {

			// We have an error from our call, update UI with an error so that user knows what happened
			// We could do this on the adapter or on the SearchActivity.

		} else {

			// Things went right, let's update list with loaded venues
			venueAdapter.updateApiData(((ApiData) o).getResponse());
		}
	}
}
