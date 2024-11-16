package br.com.uoutec.community.ediacaran.sales.pub.entity;

import org.brandao.brutos.annotation.Transient;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistry;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class OrderPanelPubEntity 
	extends OrderPubEntity {

	@Transient
	private static final long serialVersionUID = 7794103768364892856L;

	@Override
	protected Order reloadEntity() throws Throwable {
		OrderRegistry r = EntityContextPlugin.getEntity(OrderRegistry.class);
		return r.findById(super.getId(), SystemUserRegistry.CURRENT_USER);
	}

	protected void copyTo(Order o, boolean reload, boolean override,
			boolean validate) throws Throwable {
	}
	
}
