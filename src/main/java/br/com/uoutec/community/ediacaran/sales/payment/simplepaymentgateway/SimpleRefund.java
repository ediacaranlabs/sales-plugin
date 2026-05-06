package br.com.uoutec.community.ediacaran.sales.payment.simplepaymentgateway;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritance;

@EntityInheritance(base=Refund.class, name="simple")
public class SimpleRefund extends Refund {

	private static final long serialVersionUID = 3347471112297156243L;

	public SimpleRefund(){
		super.setRefundType("simple");
	}
	
}
