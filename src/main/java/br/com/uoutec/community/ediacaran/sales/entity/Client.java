package br.com.uoutec.community.ediacaran.sales.entity;

import java.lang.reflect.InvocationTargetException;

import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class Client extends SystemUser{

	private static final long serialVersionUID = 2658609156376778575L;

	public static final String SHIPPING = "shipping";
	
	public static final String BILLING = "billing";
	
	private Integer selectedBillingAddress;
	
	private Integer selectedShippingAddress;
	
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
			
			if(user.getAddData().get("selectedBillingAddress") != null) {
				this.selectedBillingAddress = Integer.parseInt(user.getAddData().get("selectedBillingAddress"));
			}

			if(user.getAddData().get("selectedShippingAddress") != null) {
				this.selectedShippingAddress = Integer.parseInt(user.getAddData().get("selectedShippingAddress"));
			}
			
		}
		
	}

	public Integer getSelectedBillingAddress() {
		return selectedBillingAddress;
	}

	public void setSelectedBillingAddress(Integer selectedBillingAddress) {
		this.selectedBillingAddress = selectedBillingAddress;
	}

	public Integer getSelectedShippingAddress() {
		return selectedShippingAddress;
	}

	public void setSelectedShippingAddress(Integer selectedShippingAddress) {
		this.selectedShippingAddress = selectedShippingAddress;
	}

	public static Client toClient(SystemUser user
			) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		EntityInheritanceManager entityInheritanceUtil = 
				EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		return toClient(user, entityInheritanceUtil);
	}
	
	public static Client toClient(SystemUser user, EntityInheritanceManager entityInheritanceUtil
			) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return entityInheritanceUtil
				.getInstance(
						Client.class, 
						user.getCountry().getIsoAlpha3(), 
						new Class<?>[] {SystemUser.class}, new Object[] {user}
				);
	}
	
}
