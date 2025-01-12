package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.ElectronicShippingMethod;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPubEntity;
import br.com.uoutec.community.ediacaran.sales.shipping.ElectronicShipping;
import br.com.uoutec.community.ediacaran.sales.shipping.ElectronicShippingPubEntity;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ShippingMethodPluginInstaller {

	public ShippingMethodPluginInstaller() {
	}
	
	public void install() throws Throwable {
		
		ShippingMethodRegistry smr = EntityContextPlugin.getEntity(ShippingMethodRegistry.class);
		smr.registerShippingMethod(new ElectronicShippingMethod());
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.register(ElectronicShippingPubEntity.class, "electronic", ShippingPubEntity.class);  
		eiu.register(ElectronicShipping.class, "electronic", Shipping.class);  
	}
	
	public void uninstall() throws Throwable {
		
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.removePaymentGateway("electronic");
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.remove("electronic", ShippingPubEntity.class);  
		eiu.remove("electronic", Shipping.class);  
	}
	
}
