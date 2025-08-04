package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface ShippingIndexEntityAccess {

	Shipping findById(String id) throws EntityAccessException;
	
	void save(Shipping value) throws EntityAccessException;
	
	void update(Shipping value) throws EntityAccessException;
	
	void delete(Shipping value) throws EntityAccessException;
	
	List<Shipping> search(ShippingSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
