package br.com.uoutec.community.ediacaran.sales.pub.entity;

import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=PaymentPubEntity.class, name="free")
public class FreePaymentPubEntity extends PaymentPubEntity{

	private static final long serialVersionUID = 8759543552155535393L;
	
}
