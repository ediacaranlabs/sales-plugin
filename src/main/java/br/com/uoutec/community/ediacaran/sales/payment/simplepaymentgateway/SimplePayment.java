package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=Payment.class, name="simple")
public class SimplePayment extends Payment{

	private static final long serialVersionUID = 3347471112297156243L;

	public SimplePayment(){
		super.setPaymentType("simple");
	}
}
