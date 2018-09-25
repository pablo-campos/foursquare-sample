package com.pablocampos.foursquaresample.models;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {

	String address;
	String crossStreet;
	float lat;
	float lng;
	List<LabeledLatLng> labeledLatLngs;
	String postalCode;
	String cc;
	String city;
	String state;
	String country;
	String distance;
	List<String> formattedAddress;



	public String getAddress () {
		return address;
	}



	public String getCrossStreet () {
		return crossStreet;
	}



	public float getLat () {
		return lat;
	}



	public float getLng () {
		return lng;
	}



	public List<LabeledLatLng> getLabeledLatLngs () {
		return labeledLatLngs;
	}



	public String getPostalCode () {
		return postalCode;
	}



	public String getCc () {
		return cc;
	}



	public String getCity () {
		return city;
	}



	public String getState () {
		return state;
	}



	public String getCountry () {
		return country;
	}



	public String getDistance () {
		return distance;
	}



	public String getFormattedAddress () {
		StringBuilder formattedAddressString = new StringBuilder();

		for (String str : formattedAddress){
			formattedAddressString.append(str).append("\n");
		}

		return formattedAddressString.toString();
	}
}
