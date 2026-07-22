package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;
import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Address;

public class AddressCartPubEntity extends AddressPubEntity {

	private static final long serialVersionUID = 1391868122764939558L;

	@Constructor
	public AddressCartPubEntity(){
	}
	
	public AddressCartPubEntity(Address e, Locale locale){
		super(e, locale);
	}


	@Override
	protected void preRebuild(Address instance, boolean reload, boolean override, boolean validate) {
		super.setId(0);
	}
	
	@Override
	protected Address reloadEntity() throws Throwable {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Address createNewInstance() throws Throwable {
		return new Address();
	}

	@Override
	protected void copyTo(Address o, boolean reload, boolean override,
			boolean validate) throws Throwable {
		
		o.setFirstName(super.getFirstName());
		o.setLastName(super.getLastName());
		o.setAddressLine1(super.getAddressLine1());
		o.setCity(super.getCity());
		o.setAddressLine2(super.getAddressLine2());
		o.setCountry(getCountry() == null? null : getCountry().rebuild(true, false, false));
		o.setRegion(super.getRegion());
		o.setZip(super.getZip());
		
	}
	
	@Transient
	public void setProtectedID(String protectedID) {
		super.setProtectedID(protectedID);
	}

	@Transient
	public void setDeleted(Boolean deleted) {
		super.setDeleted(deleted);
	}
	
}
