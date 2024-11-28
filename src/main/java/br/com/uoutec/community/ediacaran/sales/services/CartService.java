package br.com.uoutec.community.ediacaran.sales.services;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;

@Singleton
public class CartService {

	@Inject
	private PaymentGatewayRegistry paymentGatewayProvider;

	@Inject
	private CartRegistry cartRegistry;
	
	public List<PaymentGateway> getPaymentGateways(Cart cart, SystemUser user){
		return paymentGatewayProvider.getPaymentGateways(cart, user);
	}

	public PaymentGateway getPaymentGateway(String value) {
		return paymentGatewayProvider.getPaymentGateway(value);
	}
	
	public void setQuantity(Cart cart, String serial, 
			int quantity) throws MaxItensException, ProductTypeRegistryException, 
			ProductTypeHandlerException{
		cartRegistry.setQuantity(cart, serial, quantity);
	}
	
	public ProductRequest add(Cart cart, Product product, 
			Map<String, String> addData, int units) throws MaxItensException, 
			ProductTypeRegistryException, ProductTypeHandlerException{
		return cartRegistry.add(cart, product, addData, units);
	}
	
	public void remove(Cart cart, String serial) 
			throws ProductTypeRegistryException, ProductTypeHandlerException{
		cartRegistry.remove(cart, serial);
	}
	
	public Checkout checkout(Cart cart, SystemUser systemUser, Payment payment, String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException{
		return cartRegistry.checkout(cart, systemUser,  payment, message);
	}
	
}
