package com.pablocampos.foursquaresample.models;

import java.io.Serializable;

public class ApiData implements Serializable {

	Meta meta;
	Response response;



	public Meta getMeta () {
		return meta;
	}



	public Response getResponse () {
		return response;
	}
}
