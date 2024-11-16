package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.Cart;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface OrderRegistry extends PublicBean {

	void registerOrder(Order entity) throws OrderRegistryException;
	
	void removeOrder(Order entity) throws OrderRegistryException;

	Order createOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException, SystemUserRegistryException; 

	Order createOrder(Cart cart, SystemUserID userID, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException, SystemUserRegistryException; 
	
	Order createOrder(Cart cart, SystemUser systemUser, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException; 
	
	Invoice createInvoice(String orderID, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException;
	
	void createRefound(String order, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
			
	void revertRefound(String order, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
			
	Shipping createShipping(String order, 
			boolean useAlternativeAdress, String shippingCode) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
	
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
