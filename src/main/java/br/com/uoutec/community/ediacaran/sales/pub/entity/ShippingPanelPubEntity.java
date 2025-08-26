package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ShippingPanelPubEntity extends ShippingPubEntity {
	
	private static final long serialVersionUID = 8112064051350456421L;

	@Constructor
	public ShippingPanelPubEntity() {
		super();
	}
	
	public ShippingPanelPubEntity(Shipping e, Locale locale) {
		super(e, locale);
	}
	
	@Override
	protected boolean isEqualId(Shipping instance) throws Throwable {
		return false;
	}

	@Override
	protected boolean hasId(Shipping instance) throws Throwable {
		return false;
	}

	protected Shipping createInstance(Shipping instance, boolean reload, 
			boolean override, boolean validate) throws Throwable {
		
		Shipping e = super.createInstance(instance, reload, override, validate);
		Integer client = getCurrentUserID();
		
		if(reload) {
			
			if(!client.equals(e.getClient().getId())) {
				super.throwReloadEntityFail();
			}
			
		}
		
		return e;
	}
	
	public Integer getCurrentUserID() throws SystemUserRegistryException {
		SystemUserRegistry systemUserRegistry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		Integer userID = systemUserRegistry.getIDBySystemID(SystemUserIDProvider.getSystemUserID());
		
		if(userID == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return userID;
	}
	
}
