package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.util.Locale;

import org.brandao.brutos.annotation.Constructor;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.registry.OrderReportRegistry;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class OrderReportClientPubEntity extends OrderReportPubEntity {

	private static final long serialVersionUID = -4690036271300115574L;

	@Constructor
	public OrderReportClientPubEntity() {
		super();
	}
	
	public OrderReportClientPubEntity(OrderReport e, Locale locale) {
		super(e, locale);
	}
	
	@Override
	protected OrderReport reloadEntity() throws Throwable {
		OrderReportRegistry orderReportRegistry = EntityContextPlugin.getEntity(OrderReportRegistry.class);
		OrderReport e = orderReportRegistry.findById(getId());
		Integer currentUserID = getCurrentUserID();
		
		return e == null? null : (currentUserID.equals(e.getClient().getId())? e : null);
	}

	public static Integer getCurrentUserID() throws SystemUserRegistryException {
		SystemUserRegistry systemUserRegistry = EntityContextPlugin.getEntity(SystemUserRegistry.class);
		Integer userID = systemUserRegistry.getIDBySystemID(SystemUserIDProvider.getSystemUserID());
		
		if(userID == null) {
			throw new SystemUserRegistryException(String.valueOf(userID));
		}
		
		return userID;
	}
	
}
