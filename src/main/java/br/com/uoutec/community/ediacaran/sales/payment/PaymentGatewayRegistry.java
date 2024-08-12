package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.List;

public interface PaymentGatewayRegistry {

	PaymentGateway getPaymentGateway(String code) throws PaymentGatewayException;
	
	List<PaymentGateway> getPaymentGateways();
	
}
