package br.com.uoutec.community.ediacaran.sales.services;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.CartRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ClientRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.MaxItensException;
import br.com.uoutec.community.ediacaran.sales.registry.OrderRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeHandlerException;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;

@Singleton
public class CartService {

	@Inject
	private PaymentGatewayRegistry paymentGatewayProvider;

	@Inject
	private CartRegistry cartRegistry;
	
	@Inject
	private ProductRegistry productRegistry;
	
	@Inject
	private ClientRegistry clientRegistry;
	
	public Address getBillingAddress(Client client) throws ClientRegistryException {
		return clientRegistry.getAddress(client, Client.BILLING); 
	}

	public List<Address> getShippingAddresses(Client client) throws ClientRegistryException {
		return clientRegistry.getAddresses(client, Client.SHIPPING); 
	}

	public Address getShippingAddress(int id, Client client) throws ClientRegistryException {
		return clientRegistry.getAddressByID(id); 
	}
	
	public List<PaymentGateway> getPaymentGateways(Cart cart, Client user){
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
	
	@Transactional
	public Checkout checkout(Cart cart, Payment payment, String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException{
		return cartRegistry.checkout(cart, payment, message);
	}
	
	public ProductSearchResult search(ProductSearch value) throws ProductRegistryException {
		return productRegistry.search(value);
	}
	
}
