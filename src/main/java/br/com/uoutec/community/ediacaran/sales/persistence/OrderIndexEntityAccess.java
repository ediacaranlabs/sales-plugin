package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderSearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface OrderIndexEntityAccess {

	Order findById(Serializable id) throws EntityAccessException;

	void save(Order value) throws EntityAccessException;
	
	void update(Order value) throws EntityAccessException;
	
	void delete(Order value) throws EntityAccessException;
	
	List<Order> searchOrder(OrderSearch value, Integer firstResult, Integer max) throws EntityAccessException;
	
	void flush();
	
}
