package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.List;

import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.Registry;

public interface PaymentGatewayRegistry extends Registry, PublicBean{

	void registry(PaymentGateway paymentGateway) throws PaymentGatewayException;
	
	void remove(String value) throws PaymentGatewayException;
	
	PaymentGateway getPaymentGateway(String value);
	
	List<PaymentGateway> getPaymentGateways();
	
}
