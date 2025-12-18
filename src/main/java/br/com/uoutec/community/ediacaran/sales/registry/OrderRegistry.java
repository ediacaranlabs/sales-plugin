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
import br.com.uoutec.community.ediacaran.sales.entity.PaymentStatus;
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

	void updateStatus(Order o, OrderStatus status) throws OrderRegistryException;
	
	/* create order */
	
	Order createOrder(Cart cart, Payment payment, String message, PaymentGateway paymentGateway) throws OrderRegistryException; 

	/* register payment */
	
	void registerPendingPayment(Order o) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException;
	
	void registerPayment(Order o) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException ;
	
	void updatePaymentStatus(Payment payment, Order o, PaymentStatus paymentStatus) throws OrderRegistryException, PaymentGatewayException, ClientRegistryException;
	
	/* register invoice */
	
	Invoice createInvoice(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException;

	/* register shipping */
	
	Shipping createShipping(Order order, Map<String, Integer> itens, String message) throws OrderRegistryException;

	/* refound payment */
	
	void createRefound(String order, String message) throws RegistryException;
			
	void revertRefound(String order, String message) throws RegistryException;
			
	boolean isAvailability(Cart cart) 
			throws ProductTypeHandlerException, ProductTypeRegistryException, 
			OrderRegistryException, SystemUserRegistryException;
	
	Order findById(String id) throws OrderRegistryException;

	Order findById(String id, SystemUserID userID) throws OrderRegistryException;
	
	Order findById(String id, SystemUser systemUser) throws OrderRegistryException;
	
	Order findByCartID(String id) throws OrderRegistryException;

	OrderResultSearch searchOrder(OrderSearch value) throws OrderRegistryException;
	
	List<Order> getOrders(OrderStatus status, Integer first, 
			Integer max) throws OrderRegistryException, SystemUserRegistryException;

	List<Order> getOrders(Integer first, 
			Integer max) throws OrderRegistryException, SystemUserRegistryException;
	
	List<Order> getOrders(SystemUserID userID, Integer first, Integer max) throws OrderRegistryException, SystemUserRegistryException;

	List<Order> getOrders(SystemUserID userID, OrderStatus status, Integer first, Integer max) throws OrderRegistryException, SystemUserRegistryException;

	/* product */
	
	ProductRequest getProductRequest(String orderID, String id) throws OrderRegistryException;

	/* log */
	
	void registryLog(Order order, String message) throws OrderRegistryException;

	/*
	void updateLog(OrderLog log) throws OrderRegistryException;
	
	void deleteLog(OrderLog log) throws OrderRegistryException;
	*/
	
	List<OrderLog> getLogs(String orderID, Integer first, Integer max) throws OrderRegistryException;
	
}
