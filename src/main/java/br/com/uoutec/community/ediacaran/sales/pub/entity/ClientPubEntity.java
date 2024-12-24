package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.front.pub.GenericPubEntity;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.system.util.SecretUtil;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserPubEntity;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ClientPubEntity extends SystemUserPubEntity{

	private static final long serialVersionUID = 1391868122764939558L;

	private String selectedBillingAddress;
	
	private String selectedShippingAddress;
	
	@Constructor
	public ClientPubEntity(){
		super.setActivated(false);
	}
	
	public ClientPubEntity(SystemUser e, Locale locale){
		super(e, locale);
		super.setActivated(false);
	}
	
	@Transient
	public void setActivated(Boolean activated) {
		super.setActivated(activated);
	}
	
	public String getSelectedBillingAddress() {
		return selectedBillingAddress;
	}

	public void setSelectedBillingAddress(String selectedBillingAddress) {
		this.selectedBillingAddress = selectedBillingAddress;
	}

	public String getSelectedShippingAddress() {
		return selectedShippingAddress;
	}

	public void setSelectedShippingAddress(String selectedShippingAddress) {
		this.selectedShippingAddress = selectedShippingAddress;
	}

	@Override
	protected void copyTo(SystemUser x, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		SubjectProvider subjectProvider = EntityContextPlugin.getEntity(SubjectProvider.class); 
		Subject subject = subjectProvider.getSubject();
		
		Client o = (Client)x;
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.EMAIL)) {
			o.setEmail(super.getEmail());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.FIRST_NAME)) {
			o.setFirstName(super.getFirstName());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.LAST_NAME)) {
			o.setLastName(super.getLastName());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ADDRESS_LINE1)) {
			o.setAddressLine1(super.getAddressLine1());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.CITY)) {
			o.setCity(super.getCity());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ADDRESS_LINE2)) {
			o.setAddressLine2(super.getAddressLine2());
		}
		
		if(super.getCountry() != null && subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.COUNTRY)) {
			o.setCountry(getCountry().rebuild(true, false, false));
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ORGANIZATION)) {
			o.setOrganization(super.getOrganization());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.PHONE)) {
			o.setPhone(super.getPhone());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.REGION)) {
			o.setRegion(super.getRegion());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ZIP)) {
			o.setZip(super.getZip());
		}

		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.BILLING_ADDRESS)) {
			if(this.selectedBillingAddress != null) {
				o.setSelectedBillingAddress(Integer.parseInt(SecretUtil.toID(this.selectedBillingAddress)));
			}
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.SHIPPING_ADDRESSES)) {
			if(this.selectedShippingAddress != null) {
				o.setSelectedShippingAddress(Integer.parseInt(SecretUtil.toID(this.selectedShippingAddress)));
			}
		}
		
	}
	
	protected SystemUser reloadEntity() throws Throwable {
		ClientRegistry registry = EntityContextPlugin.getEntity(ClientRegistry.class);
		Client user = registry.findById(super.getId());
		
		/*
		if(user != null && !user.getClass().isAssignableFrom(Client.class)) {
			user = new Client(user);
		}
		*/
		return user;
		
	}
	
	@Override
	protected SystemUser createNewInstance() throws Throwable {
		return new Client();
	}
	
	protected Class<?> getGenericType() {
		return ClientPubEntity.class;
	}

	protected void loadProperties(GenericPubEntity<SystemUser> e) {
		super.loadProperties(e);
		ClientPubEntity u = (ClientPubEntity)e;
		this.selectedBillingAddress = u.selectedBillingAddress;
		this.selectedShippingAddress = u.selectedShippingAddress;
	}
	
}
