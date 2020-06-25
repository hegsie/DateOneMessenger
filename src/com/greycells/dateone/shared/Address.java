package com.greycells.dateone.shared;

import java.io.Serializable;

public class Address implements Serializable {
	public Address(){
		
	}
	
	public Address(String housenumber, String street, String city, String county, String postcode) {
		this.houseNumber = housenumber;
		this.street = street;
		this.city = city;
		this.county = county;
		this.postcode = postcode;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 6700794462985312573L;
	public String houseNumber;
	public String street;
	public String city;
	public String county;
	public String postcode;
}
