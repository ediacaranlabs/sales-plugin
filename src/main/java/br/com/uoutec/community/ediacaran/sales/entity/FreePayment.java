package br.com.uoutec.community.ediacaran.sales.entity;

import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=Payment.class, name="Free")
public class FreePayment extends Payment{

	private static final long serialVersionUID = 3347471112297156243L;

	public FreePayment(){
		super();
		super.setPaymentType("Free");
	}
}
