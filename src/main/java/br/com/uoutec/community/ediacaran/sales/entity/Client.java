package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class Client extends SystemUser{

	private static final long serialVersionUID = 2658609156376778575L;

	public Client() {
	}
	
	public Client(SystemUser user) {
		this.activated = user.isActivated();
		this.addData = user.getAddData();
		this.address = user.getAddress();
		this.city = user.getCity();
		this.complement = user.getComplement();
		this.country = user.getCountry();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.id = user.getId();
		this.language = user.getLanguage();
		this.lastName = user.getLastName();
		this.organization = user.getOrganization();
		this.phone = user.getPhone();
		this.region = user.getRegion();
		this.template = user.getTemplate();
		this.zip = user.getZip();
	}

}
