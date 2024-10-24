package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Checkout;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.entity.registry.Registry;

public interface CartRegistry extends Registry{

	public static final int CART_MAX_ITENS = 10;
	
	boolean isEnabledShippingSupport();

	void setEnabledShippingSupport(boolean value);

	String getShippingPublicResource();

	void setShippingPublicResource(String value);
	
	boolean isEnabledCouponSupport();

	void setEnabledCouponSupport(boolean value);

	String getCouponPublicResource();

	void setCouponPublicResource(String couponPublicResource);
	
	Cart getNewCart();
	
	void destroy(Cart cart);

	boolean isAvailability(Cart cart) 
			throws ProductTypeRegistryException, ProductTypeHandlerException, SystemUserRegistryException;
			
	void remove(Cart cart, String serial) 
			throws ProductTypeRegistryException, ProductTypeHandlerException;
	
	void setQuantity(Cart cart, String serial, 
			int quantity) throws MaxItensException, ProductTypeRegistryException, 
			ProductTypeHandlerException;
	
	ProductRequest add(Cart cart, Product product, 
			Map<String, String> addData, int units) throws MaxItensException, 
			ProductTypeRegistryException, ProductTypeHandlerException;
	
	Checkout checkout(Cart cart, SystemUserID userID, Payment payment, 
			String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException;
	
	Checkout checkout(Cart cart,Payment payment, 
			String message) throws
			OrderRegistryException, PaymentGatewayException, SystemUserRegistryException;
	
	Checkout checkout(Cart cart, SystemUser user, Payment payment, 
			String message) throws OrderRegistryException, PaymentGatewayException;
	
	void calculateTotal(Cart cart);
	
}
