package br.com.uoutec.community.ediacaran.sales.payment;

import br.com.uoutec.community.ediacaran.sales.entity.Payment;

public interface PaymentGateway {

	public static final String TYPE_NAME = "PAYMENT_GATEWAY";
	
	void payment(int owner, String order, Payment payment) throws PaymentGatewayException;
	
	String redirectView(int owner, String order) throws PaymentGatewayException;
	
	String getOwnerView(int owner, String order) throws PaymentGatewayException;
	
	String getView(int owner, String order) throws PaymentGatewayException;

	String getId();
	
	String getName();
	
}
