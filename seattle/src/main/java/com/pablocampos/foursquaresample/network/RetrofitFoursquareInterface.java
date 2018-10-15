package com.pablocampos.foursquaresample.network;

import com.pablocampos.foursquaresample.models.ApiData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitFoursquareInterface {

	@GET("/v2/venues/search?")
	Call<ApiData> getVenues (@Query("client_id") String client_id, @Query("client_secret") String client_secret, @Query("near") String near, @Query("query") String query, @Query("v") String v, @Query("limit") String limit, @Query("ll") String ll);
}
