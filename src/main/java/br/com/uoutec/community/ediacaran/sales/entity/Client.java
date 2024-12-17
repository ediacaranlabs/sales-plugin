package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public class Client extends SystemUser{

	private static final long serialVersionUID = 2658609156376778575L;

	public static final String SHIPPING = "shipping";
	
	public static final String BILLING = "billing";
	
	private boolean useDefaultBillingAddress;
	
	private boolean useDefaultShippingAddress;
	
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

		if(user.getAddData() != null) {
			this.useDefaultBillingAddress = "true".equals(user.getAddData().get("useDefaultBillingAddress")) || user.getAddData().get("useDefaultBillingAddress") == null;
			this.useDefaultShippingAddress = "true".equals(user.getAddData().get("useDefaultShippingAddress")) || user.getAddData().get("useDefaultShippingAddress") == null;
		}
		
	}

	public boolean isUseDefaultBillingAddress() {
		return useDefaultBillingAddress;
	}

	public void setUseDefaultBillingAddress(boolean useDefaultBillingAddress) {
		this.useDefaultBillingAddress = useDefaultBillingAddress;
	}

	public boolean isUseDefaultShippingAddress() {
		return useDefaultShippingAddress;
	}

	public void setUseDefaultShippingAddress(boolean useDefaultShippingAddress) {
		this.useDefaultShippingAddress = useDefaultShippingAddress;
	}

}
