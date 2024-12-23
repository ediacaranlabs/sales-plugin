package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class Client extends SystemUser{

	private static final long serialVersionUID = 2658609156376778575L;

	public static final String SHIPPING = "shipping";
	
	public static final String BILLING = "billing";
	
	private transient Address billingAddress;
	
	private transient Address shippingAddress;
	
	public Client() {
	}
	
	public Client(SystemUser user) {
		this.activated = user.isActivated();
		this.addData = user.getAddData();
		this.addressLine1 = user.getAddressLine1();
		this.city = user.getCity();
		this.addressLine2 = user.getAddressLine2();
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

		if(user.getAddData() != null) {
			if(user.getAddData().get("billingAddressId") != null) {
				this.billingAddress = new Address();
				this.billingAddress.setId(Integer.parseInt(user.getAddData().get("billingAddressId")));
			}

			if(user.getAddData().get("shippingAddressId") != null) {
				this.billingAddress = new Address();
				this.billingAddress.setId(Integer.parseInt(user.getAddData().get("shippingAddressId")));
			}
			
		}
		
	}

	public String getBillingAddressId() {
		return this.billingAddress == null || billingAddress.getId() <= 0? null : String.valueOf(this.billingAddress.getId());
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getShippinggAddressId() {
		return this.shippingAddress == null || shippingAddress.getId() <= 0? null : String.valueOf(this.shippingAddress.getId());
	}
	
	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

}
