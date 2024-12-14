package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserPubEntity;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ClientPubEntity extends SystemUserPubEntity{

	private static final long serialVersionUID = 1391868122764939558L;

	private Boolean useDefaultBillingAddress;
	
	private Boolean useDefaultShippingAddress;
	
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
	
	public Boolean getUseDefaultBillingAddress() {
		return useDefaultBillingAddress;
	}

	public void setUseDefaultBillingAddress(Boolean useDefaultBillingAddress) {
		this.useDefaultBillingAddress = useDefaultBillingAddress;
	}

	public Boolean getUseDefaultShippingAddress() {
		return useDefaultShippingAddress;
	}

	public void setUseDefaultShippingAddress(Boolean useDefaultShippingAddress) {
		this.useDefaultShippingAddress = useDefaultShippingAddress;
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
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.ADDRESS)) {
			o.setAddress(super.getAddress());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.CITY)) {
			o.setCity(super.getCity());
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.COMPLEMENT)) {
			o.setComplement(super.getComplement());
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
			o.setUseDefaultBillingAddress(useDefaultBillingAddress == null? true : useDefaultBillingAddress);
		}
		
		if(subject.isPermitted(SalesUserPermissions.CLIENT.FIELDS.SHIPPING_ADDRESSES)) {
			o.setUseDefaultShippingAddress(useDefaultShippingAddress == null? true : useDefaultShippingAddress);
		}
	}
	
	protected SystemUser reloadEntity() throws Throwable {
		ClientRegistry registry = EntityContextPlugin.getEntity(ClientRegistry.class);
		return registry.findById(super.getId());
	}
	
	@Override
	protected SystemUser createNewInstance() throws Throwable {
		return new Client();
	}
	
	protected Class<?> getGenericType() {
		return ClientPubEntity.class;
	}
	
}
