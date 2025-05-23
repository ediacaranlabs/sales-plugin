package br.com.uoutec.community.ediacaran.sales.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.entity.registry.DataValidation;
import br.com.uoutec.entity.registry.IdValidation;

public class Address implements Serializable{

	private static final long serialVersionUID = 422874914019058641L;

	@Min(value=1, groups = IdValidation.class)
	protected int id;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String firstName;

	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String lastName;
	
	@NotNull(groups={DataValidation.class})
	protected Country country;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.ADDRESS_FORMAT)
	protected String addressLine1;
	
	@Pattern(regexp=CommonValidation.ADDRESS_FORMAT)
	protected String addressLine2;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String city;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String region;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.ZIP)
	protected String zip;
	
	public Address() {
	}

	public Address(Address address) {
		this.addressLine1 = address.getAddressLine1();
		this.addressLine2 = address.getAddressLine2();
		this.city = address.getCity();
		this.country = address.getCountry();
		this.firstName = address.getFirstName();
		this.id = address.getId();
		this.lastName = address.getLastName();
		this.region = address.getRegion();
		this.zip = address.getZip();
	}
	
	public String getProtectedID() {
		return id <= 0? null : SecretUtil.toProtectedID(String.valueOf(id));		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}


	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
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
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if(addressLine1 != null) {
			builder = builder.length() == 0? builder.append(addressLine1) : builder.append(" ").append(addressLine1);
		}
		
		if(addressLine2 != null) {
			builder = builder.length() == 0? builder.append(addressLine2) : builder.append(" ").append(addressLine2);
		}
		
		if(region != null) {
			builder = builder.length() == 0? builder.append(region) : builder.append(" ").append(region);
		}

		if(city != null) {
			builder = builder.length() == 0? builder.append(city) : builder.append(" ").append(city);
		}
		
		if(country != null) {
			builder = builder.length() == 0? builder.append(country.getName()) : builder.append(" ").append(country.getName());
		}
		
		return builder.toString();
	}

}
