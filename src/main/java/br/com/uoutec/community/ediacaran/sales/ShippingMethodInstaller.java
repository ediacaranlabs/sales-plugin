package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.pub.entity.ShippingPubEntity;
import br.com.uoutec.community.ediacaran.sales.shipping.ShippingMethodRegistry;
import br.com.uoutec.community.ediacaran.sales.shipping.flatrate.FlatRateShipping;
import br.com.uoutec.community.ediacaran.sales.shipping.flatrate.FlatRateShippingMethod;
import br.com.uoutec.community.ediacaran.sales.shipping.flatrate.FlatRateShippingPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class ShippingMethodInstaller {

	public void install() throws Throwable{
		FlatRateShippingMethod smm = new FlatRateShippingMethod();
		ShippingMethodRegistry smr = EntityContextPlugin.getEntity(ShippingMethodRegistry.class);
		smr.registerShippingMethod(smm);
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		
		eiu.register(FlatRateShipping.class, "flatrate", Shipping.class);  
		eiu.register(FlatRateShippingPubEntity.class, "flatrate", ShippingPubEntity.class);  
	}
	
	public void uninstall() throws Throwable{
		ShippingMethodRegistry smr = EntityContextPlugin.getEntity(ShippingMethodRegistry.class);
		smr.removeShippingMethod("flatrate");
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.remove("flatrate", ShippingPubEntity.class);  
		eiu.remove("flatrate", Shipping.class);  
	}
	
}
