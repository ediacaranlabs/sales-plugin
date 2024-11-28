package br.com.uoutec.community.ediacaran.sales.payment;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.Registry;

public interface PaymentGatewayRegistry extends Registry, PublicBean{

	void registerPaymentGateway(PaymentGateway paymentGateway) throws PaymentGatewayException;
	
	void removePaymentGateway(String value) throws PaymentGatewayException;
	
	PaymentGateway getPaymentGateway(String value);
	
	List<PaymentGateway> getPaymentGateways(Cart cart, SystemUser user);
	
}
