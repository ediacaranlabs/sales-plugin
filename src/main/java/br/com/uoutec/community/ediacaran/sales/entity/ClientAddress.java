package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;

public class ClientAddress  extends Address {
	
	private static final long serialVersionUID = -8338181714891282060L;
	
	private Address address;
	
	private int client;

	public ClientAddress(Address address, Client client) {
		this.address = address;
		this.client = client == null? null : client.getId();
		
	}
	
	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	public String getProtectedID() {
		return address.getProtectedID();
	}

	public int getId() {
		return address.getId();
	}

	public void setId(int id) {
		address.setId(id);
	}

	public String getFirstName() {
		return address.getFirstName();
	}

	public void setFirstName(String firstName) {
		address.setFirstName(firstName);
	}

	public String getLastName() {
		return address.getLastName();
	}

	public void setLastName(String lastName) {
		address.setLastName(lastName);
	}

	public Country getCountry() {
		return address.getCountry();
	}

	public void setCountry(Country country) {
		address.setCountry(country);
	}

	public String getAddressLine1() {
		return address.getAddressLine1();
	}

	public int hashCode() {
		return address.hashCode();
	}

	public void setAddressLine1(String addressLine1) {
		address.setAddressLine1(addressLine1);
	}

	public String getAddressLine2() {
		return address.getAddressLine2();
	}

	public void setAddressLine2(String addressLine2) {
		address.setAddressLine2(addressLine2);
	}

	public String getCity() {
		return address.getCity();
	}

	public void setCity(String city) {
		address.setCity(city);
	}

	public String getRegion() {
		return address.getRegion();
	}

	public void setRegion(String region) {
		address.setRegion(region);
	}

	public String getZip() {
		return address.getZip();
	}

	public void setZip(String zip) {
		address.setZip(zip);
	}

	public boolean equals(Object obj) {
		return address.equals(obj);
	}

	public String toString() {
		return address.toString();
	}
	
}
