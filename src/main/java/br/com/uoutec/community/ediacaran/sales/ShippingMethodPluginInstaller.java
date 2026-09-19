package br.com.uoutec.community.ediacaran.sales;

public class ShippingMethodPluginInstaller {

	public ShippingMethodPluginInstaller() {
	}
	
	public void install() throws Throwable {
		
		//ShippingMethodRegistry smr = EntityContextPlugin.getEntity(ShippingMethodRegistry.class);
		//smr.registerShippingMethod(new ElectronicShippingMethod());
		
		//EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		//eiu.register(ElectronicShippingPubEntity.class, "electronic", ShippingPubEntity.class);  
		//eiu.register(ElectronicShipping.class, "electronic", Shipping.class);  
	}
	
	public void uninstall() throws Throwable {
		
		//PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		//pgr.removePaymentGateway("electronic");
		
		//EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		//eiu.remove("electronic", ShippingPubEntity.class);  
		//eiu.remove("electronic", Shipping.class);  
	}
	
}
