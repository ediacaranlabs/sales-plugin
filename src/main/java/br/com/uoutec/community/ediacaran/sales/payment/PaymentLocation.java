package br.com.uoutec.community.ediacaran.sales.payment;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;

public class PaymentLocation {

	private Country country;

	private String city;
	
	private String region;
	
	private String zip;

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
}
