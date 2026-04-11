package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.persistence.EntityAccessException;

public interface RefundEntityAccess {

	Refund findById(String id) throws EntityAccessException;
	
	void save(Refund value) throws EntityAccessException;
	
	void update(Refund value) throws EntityAccessException;
	
	void delete(Refund value) throws EntityAccessException;
	
	List<Refund> getList(Integer first, Integer max, Client client) throws EntityAccessException;
	
	List<Refund> findByOrder(String order, Client client) throws EntityAccessException;
	
	void flush();
	
}
