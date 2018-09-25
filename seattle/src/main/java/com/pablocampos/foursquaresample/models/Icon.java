package com.pablocampos.foursquaresample.models;

import java.io.Serializable;

public class Icon implements Serializable {

	final String ICON_SIZE = "44";
	final String FRAY_BACKGROUND = "bg_";

	String prefix;
	String suffix;

	public String getUrl(){
		return prefix + FRAY_BACKGROUND + ICON_SIZE + suffix;
	}
}
