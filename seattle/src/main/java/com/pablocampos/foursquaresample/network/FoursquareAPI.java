package com.pablocampos.foursquaresample.network;

import com.google.gson.Gson;
import com.pablocampos.foursquaresample.models.ApiData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoursquareAPI {


	public static ApiData getData (URL url) {

		try {

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			StringBuffer json = new StringBuffer(1024);
			String tmp;
			while((tmp = reader.readLine()) != null)
				json.append(tmp).append("\n");
			reader.close();

			// Deserialize data
			Gson gson = new Gson();
			ApiData apiData = gson.fromJson(json.toString(), ApiData.class);

			// The value "code" inside "meta" needs to be a 200 signaling a success call
			if (apiData == null || apiData.getMeta() == null || apiData.getMeta().getCode() != 200){
				return null;
			}

			return apiData;

		} catch (Exception e){
			return null;
		}
	}
}
