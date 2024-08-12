package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.List;

public interface PaymentGatewayRegistry {

	void registry(String name, Class<? extends PaymentGateway> paymentGateway) throws PaymentGatewayException;
	
	void remove(String name) throws PaymentGatewayException;
	
	PaymentGateway getPaymentGateway(String code) throws PaymentGatewayException;
	
	List<PaymentGateway> getPaymentGateways();
	
}
