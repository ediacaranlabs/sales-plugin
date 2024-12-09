package br.com.uoutec.community.ediacaran.sales.pub.entity;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.persistence.entity.Country;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.sales.SalesUserPermissions;
import br.com.uoutec.community.ediacaran.security.Subject;
import br.com.uoutec.community.ediacaran.security.SubjectProvider;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.pub.entity.SystemUserPubEntity;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.entityresourcebundle.EntityResourceBundle;

public class ClientPubEntity extends SystemUserPubEntity{

	private static final long serialVersionUID = 1391868122764939558L;

	@Constructor
	public ClientPubEntity(){
		super.setActivated(false);
	}
	
	public ClientPubEntity(SystemUser e){
		super(e);
		super.setActivated(false);
	}
	
	@Transient
	public void setActivated(Boolean activated) {
		super.setActivated(activated);
	}
	
	@Override
	protected void copyTo(SystemUser o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		SubjectProvider subjectProvider = EntityContextPlugin.getEntity(SubjectProvider.class); 
		Subject subject = subjectProvider.getSubject();
		
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
			CountryRegistry registry = EntityContextPlugin.getEntity(CountryRegistry.class);
			Country c = registry.getCountryByIsoAlpha3(super.getCountry());
			c = EntityResourceBundle.getEntity(c, Country.class, null);
			o.setCountry(c);
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

	}
	
	protected Class<?> getGenericType() {
		return ClientPubEntity.class;
	}
	
}
