package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderLog;
import br.com.uoutec.community.ediacaran.sales.entity.OrderResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.persistence.EntityAccessException;

public interface OrderEntityAccess {

	Order findById(Serializable id) throws EntityAccessException;
	
	Order findByCartID(String cartID) throws EntityAccessException;
	
	void save(Order value) throws EntityAccessException;
	
	void update(Order value) throws EntityAccessException;
	
	void delete(Order value) throws EntityAccessException;
	
	List<OrderResultSearch> searchOrder(OrderSearch value, Integer firstResult, Integer max) throws EntityAccessException;
	
	List<Order> getOrders(Integer owner, Integer first, Integer max) throws EntityAccessException;
	
	List<Order> getOrders(Integer owner, OrderStatus status, Integer first, Integer max) throws EntityAccessException;

	List<Order> getOrders(OrderStatus status, Integer first, Integer max) throws EntityAccessException;

	ProductRequest getProductRequest(String orderID, String id) throws EntityAccessException;
	
	void registryLog(Order order, String message) throws EntityAccessException;
	
	void updateLog(OrderLog log) throws EntityAccessException;
	
	void deleteLog(OrderLog log) throws EntityAccessException;
	
	List<OrderLog> getLogs(Order order, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
