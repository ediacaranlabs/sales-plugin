package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.pub.entity.PaymentPubEntity;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=PaymentPubEntity.class, name="simple")
public class SimplePaymentPubEntity extends PaymentPubEntity{

	private static final long serialVersionUID = 8759543552155535393L;

	protected Payment createNewInstance() throws Throwable {
		return new SimplePayment();
	}
	
}
