package br.com.uoutec.community.ediacaran.sales.payment;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;

public interface PaymentGateway {

	public static final String TYPE_NAME = "PAYMENT_GATEWAY";
	
	void payment(SystemUser user, Order order, Payment payment) throws PaymentGatewayException;
	
	String redirectView(String orderID) throws PaymentGatewayException;
	
	String redirectView(SystemUser user, Order order) throws PaymentGatewayException;
	
	String getOwnerView(Order order) throws PaymentGatewayException;
	
	String getOwnerView(SystemUser user, Order order) throws PaymentGatewayException;
	
	String getView() throws PaymentGatewayException;

	String getId();
	
	String getName();
	
}
