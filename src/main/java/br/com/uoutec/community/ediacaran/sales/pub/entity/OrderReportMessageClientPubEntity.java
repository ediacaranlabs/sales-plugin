package br.com.uoutec.community.ediacaran.sales.pub.entity;

import java.time.LocalDateTime;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.community.ediacaran.user.SystemUserIDProvider;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.pub.entity.InvalidRequestException;

public class OrderReportMessageClientPubEntity extends OrderReportMessagePubEntity {

	private static final long serialVersionUID = 7678016257006479355L;

	protected void preRebuild(OrderReportMessage instance, boolean reload, boolean override, boolean validate) {
		try {
			super.setId(null);
			super.setDate(LocalDateTime.now());
			super.setUser(getCurrentUserID());
		}
		catch(Throwable ex) {
			throw new InvalidRequestException(ex);
		}
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
