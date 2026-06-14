package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway.SimpleRefund;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=RefundPubEntity.class, name="simple")
public class SimpleRefundPubEntity extends RefundPubEntity{

	private static final long serialVersionUID = 8759543552155535393L;

	protected Refund createNewInstance() throws Throwable {
		return new SimpleRefund();
	}
	
}
