package com.pablocampos.foursquaresample.models;

import java.io.Serializable;

public class Category implements Serializable {

	String id;
	String name;
	String pluralName;
	String shortName;
	Icon icon;
	boolean primary;



	public String getId () {
		return id;
	}



	public String getName () {
		return name;
	}



	public String getPluralName () {
		return pluralName;
	}



	public String getShortName () {
		return shortName;
	}



	public Icon getIcon () {
		return icon;
	}



	public boolean isPrimary () {
		return primary;
	}
}
