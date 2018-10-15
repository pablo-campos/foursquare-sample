package com.pablocampos.foursquaresample.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFoursquareApi {

	private static final String BASE_URL = "https://api.foursquare.com";

	private static Retrofit retrofit;





	public static Retrofit getClient() {

		if (retrofit == null) {
			retrofit = new retrofit2.Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		return retrofit;
	}

}
