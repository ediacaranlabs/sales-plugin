package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.FreePayment;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.payment.FreePaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.FreePaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class PaymentGatewayPluginInstaller {

	public PaymentGatewayPluginInstaller() {
	}
	
	public void install() throws Throwable {
		
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.registerPaymentGateway(new FreePaymentGateway());
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.register(FreePaymentPubEntity.class, "free", PaymentPubEntity.class);  
		eiu.register(FreePayment.class, "free", Payment.class);  
	}
	
	public void uninstall() throws Throwable {
		
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.removePaymentGateway("free");
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.remove("free", PaymentPubEntity.class);  
		eiu.remove("free", Payment.class);  
	}
	
}
