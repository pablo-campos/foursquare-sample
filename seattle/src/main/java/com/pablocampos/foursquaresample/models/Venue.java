package com.pablocampos.foursquaresample.models;

import java.io.Serializable;
import java.util.List;

public class Venue implements Serializable {

	String id;
	String name;
	Contact contact;
	Location location;
	List<Category> categories;
	boolean vefied;
	Statistics stats;
	String url;
	boolean allowMenuUrlEdit;
	BeenHere beenHere;
	Specials specials;
	String storeId;
	HereNow hereNow;
	String referralId;
	List<Chain> venueChains;
	boolean hasPerk;



	public String getId () {
		return id;
	}



	public String getName () {
		return name;
	}



	public Contact getContact () {
		return contact;
	}



	public Location getLocation () {
		return location;
	}



	public List<Category> getCategories () {
		return categories;
	}



	public boolean isVefied () {
		return vefied;
	}



	public Statistics getStats () {
		return stats;
	}



	public String getUrl () {
		return url;
	}



	public boolean isAllowMenuUrlEdit () {
		return allowMenuUrlEdit;
	}



	public BeenHere getBeenHere () {
		return beenHere;
	}



	public Specials getSpecials () {
		return specials;
	}



	public String getStoreId () {
		return storeId;
	}



	public HereNow getHereNow () {
		return hereNow;
	}



	public String getReferralId () {
		return referralId;
	}



	public List<Chain> getVenueChains () {
		return venueChains;
	}



	public boolean isHasPerk () {
		return hasPerk;
	}
}
