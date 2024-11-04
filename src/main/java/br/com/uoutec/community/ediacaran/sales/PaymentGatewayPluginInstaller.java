package br.com.uoutec.community.ediacaran.sales;

import java.util.Arrays;

import br.com.uoutec.community.ediacaran.sales.entity.FreePayment;
import br.com.uoutec.community.ediacaran.sales.payment.FreePaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.pub.entity.FreePaymentPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class PaymentGatewayPluginInstaller {

	public PaymentGatewayPluginInstaller() {
	}
	
	public void install() throws Throwable {
		
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.registry(new FreePaymentGateway());
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.loadEntities(Arrays.asList(
				FreePaymentPubEntity.class, 
				FreePayment.class
		));
	}
	
	public void uninstall() throws Throwable {
		
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.remove("free");
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.removeEntities(Arrays.asList(
				FreePaymentPubEntity.class, 
				FreePayment.class
		));
	}
	
}
