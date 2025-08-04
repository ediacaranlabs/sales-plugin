package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.persistence.EntityAccessException;

public interface ShippingEntityAccess {

	Shipping findById(String id) throws EntityAccessException;
	
	void save(Shipping value) throws EntityAccessException;
	
	void update(Shipping value) throws EntityAccessException;
	
	void delete(Shipping value) throws EntityAccessException;
	
	List<Shipping> getList(Integer first, Integer max, Client client) throws EntityAccessException;
	
	List<Shipping> findByOrder(String order, Client client) throws EntityAccessException;
	
	void flush();
	
}
