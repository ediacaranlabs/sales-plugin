package br.com.uoutec.community.ediacaran.sales.pub.entity;

import javax.resource.spi.IllegalStateException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import br.com.uoutec.application.validation.CommonValidation;
import br.com.uoutec.community.ediacaran.sales.entity.ClientSearch;
import br.com.uoutec.pub.entity.AbstractPubEntity;

public class ClientSearchPubEntity extends AbstractPubEntity<ClientSearch> {

	private static final long serialVersionUID = 7674988526885634067L;

	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	private String fullName;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	private String firstName;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	private String lastName;

	@Pattern(regexp=CommonValidation.EMAIL)
	private String email;
	
	@Size(min=3,max=3)
	private String country;
	
	@Pattern(regexp=CommonValidation.NAME_FORMAT)
	private String city;

	private Integer page;
	
	@Max(100)
	private Integer resultPerPage;
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getResultPerPage() {
		return resultPerPage;
	}

	public void setResultPerPage(Integer resultPerPage) {
		this.resultPerPage = resultPerPage;
	}
	
	@Override
	protected boolean isEqualId(ClientSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(ClientSearch instance) throws Throwable {
		return false;
	}

	@Override
	protected ClientSearch reloadEntity() throws Throwable {
		throw new IllegalStateException();
	}

	@Override
	protected void throwReloadEntityFail() throws Throwable {
	}

	@Override
	protected ClientSearch createNewInstance() throws Throwable {
		return new ClientSearch();
	}

	@Override
	protected void copyTo(ClientSearch o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		o.setFullName(fullName);
		o.setCity(city);
		o.setCountry(country);
		o.setEmail(email);
		o.setFirstName(firstName);
		o.setLastName(lastName);
	}
	
}
