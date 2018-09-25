package com.pablocampos.foursquaresample.models;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {

	List<Venue> venues;



	public List<Venue> getVenues () {
		return venues;
	}
}
