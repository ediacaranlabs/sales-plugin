package br.com.uoutec.community.ediacaran.sales.registry;

import java.math.BigDecimal;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGatewayException;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.RegistryException;

public interface OrderRegistry extends PublicBean {

	void registerOrder(Order entity) throws OrderRegistryException;
	
	void removeOrder(Order entity) throws OrderRegistryException;

	Order createOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException, SystemUserRegistryException; 

	Order createOrder(Cart cart, SystemUserID userID, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException, SystemUserRegistryException; 
	
	Order createOrder(Cart cart, SystemUser systemUser, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException; 
	
	void registerPayment(Order o, String currency, BigDecimal value
			) throws OrderRegistryException, PaymentGatewayException;
	
	void createRefound(String order, String message) throws RegistryException;
			
	void revertRefound(String order, String message) throws RegistryException;
			
	Shipping createShipping(String order, 
			boolean useAlternativeAdress, String shippingCode) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
	
	boolean isAvailability(Cart cart, SystemUserID userID) 
			throws ProductTypeHandlerException, ProductTypeRegistryException, 
			OrderRegistryException, SystemUserRegistryException;
	
	Order findById(String id) throws OrderRegistryException;

	Order findById(String id, SystemUserID userID) throws OrderRegistryException;
	
	Order findById(String id, SystemUser systemUser) throws OrderRegistryException;
	
	Order findByCartID(String id) throws OrderRegistryException;

	List<OrderResultSearch> searchOrder(OrderSearch value, Integer first, Integer max) throws OrderRegistryException;
	
	List<Order> getOrders(OrderStatus status, Integer first, 
			Integer max) throws OrderRegistryException, SystemUserRegistryException;

	List<Order> getOrders(Integer first, 
			Integer max) throws OrderRegistryException, SystemUserRegistryException;
	
	List<Order> getOrders(SystemUserID userID, Integer first, Integer max) throws OrderRegistryException, SystemUserRegistryException;

	List<Order> getOrders(SystemUserID userID, OrderStatus status, Integer first, Integer max) throws OrderRegistryException, SystemUserRegistryException;

	/* product */
	
	ProductRequest getProductRequest(String orderID, String id) throws OrderRegistryException;

	/* log */
	
	void registryLog(String orderID, String message) throws OrderRegistryException;

	/*
	void updateLog(OrderLog log) throws OrderRegistryException;
	
	void deleteLog(OrderLog log) throws OrderRegistryException;
	*/
	
	List<OrderLog> getLogs(String orderID, Integer first, Integer max) throws OrderRegistryException;
	
}
