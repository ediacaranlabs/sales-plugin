package br.com.uoutec.community.ediacaran.sales.payment;

public interface PaymentGatewayConfigurerProvider extends PaymentGatewayRegistry{

	void registry(String name, Class<? extends PaymentGateway> paymentGateway) throws PaymentGatewayException;
	
	void remove(String name) throws PaymentGatewayException;
	
}
