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
	protected Integer id;
	
	@NotNull(groups={DataValidation.class})
	private Integer owner;
	
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
	protected String address;
	
	@Pattern(regexp=CommonValidation.ADDRESS_FORMAT)
	protected String complement;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String city;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	protected String region;
	
	@NotNull(groups={DataValidation.class})
	@Pattern(regexp=CommonValidation.ZIP)
	protected String zip;
	
	@NotNull(groups={DataValidation.class})
	protected String type;
	
	public Address() {
	}

	public String getProtectedID() {
		return id == null || id.intValue() <= 0? null : SecretUtil.toProtectedID(String.valueOf(id));		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
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
