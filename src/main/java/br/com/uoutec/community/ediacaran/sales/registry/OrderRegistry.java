package br.com.uoutec.community.ediacaran.sales.registry;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Lock;

import br.com.uoutec.community.ediacaran.sales.entity.Cart;
import br.com.uoutec.community.ediacaran.sales.entity.DiscountType;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.Payment;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.payment.PaymentGateway;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;

public interface OrderRegistry {

	void registryOrder(Order entity) throws OrderRegistryException;
	
	void removeOrder(Order entity) throws OrderRegistryException;

	Order createOrder(Cart cart, Payment payment, 
			String message, PaymentGateway paymentGateway) throws OrderRegistryException; 
	
	Invoice createInvoice(Order order, BigDecimal total, 
			BigDecimal discount, DiscountType discountType, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
	
	void createRefound(Order order, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
			
	void revertRefound(Order order, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
			
	Shipping createShipping(Order order, 
			boolean useAlternativeAdress, String shippingCode) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException;
	
	Order findById(String id) throws OrderRegistryException;

	Order findByCartID(String id) throws OrderRegistryException;
	
	List<Order> getOrders(OrderStatus status, Integer first, Integer max) throws OrderRegistryException;
	
	List<Order> getOrders(SystemUserID userID, Integer first, Integer max) throws OrderRegistryException;

	List<Order> getOrders(SystemUserID userID, OrderStatus status, Integer first, Integer max) throws OrderRegistryException;

	/* product */
	
	ProductRequest getProductRequest(Order entity, String id) throws OrderRegistryException;

	/* lock */
	
	Lock lockOrder(String id);

	/* log */
	
	void registryLog(String id, String message) throws OrderRegistryException;

	void updateLog(OrderLog log) throws OrderRegistryException;
	
	void deleteLog(OrderLog log) throws OrderRegistryException;
	
	List<OrderLog> getLogs(Order order, Integer first, Integer max) throws OrderRegistryException;
	
}
