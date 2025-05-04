package br.com.uoutec.community.ediacaran.sales;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway.SimplePayment;
import br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway.SimplePaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway.SimplePaymentPubEntity;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;

public class SimplePaymentGatewayInstaller {

	public void install() throws Throwable{
		SimplePaymentGateway pgm = new SimplePaymentGateway();
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.registerPaymentGateway(pgm);
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.register(SimplePaymentPubEntity.class, "simple", PaymentPubEntity.class);  
		eiu.register(SimplePayment.class, "simple", Payment.class);  
	}
	
	public void uninstall() throws Throwable{
		PaymentGatewayRegistry pgr = EntityContextPlugin.getEntity(PaymentGatewayRegistry.class);
		pgr.removePaymentGateway("simple");
		
		EntityInheritanceManager eiu = EntityContextPlugin.getEntity(EntityInheritanceManager.class);
		eiu.remove("simple", SimplePaymentPubEntity.class);  
		eiu.remove("simple", SimplePayment.class);  
	}
	
}
