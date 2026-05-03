package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.RefundSearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface RefundIndexEntityAccess {

	Refund findById(String id) throws EntityAccessException;
	
	void save(Refund value) throws EntityAccessException;
	
	void update(Refund value) throws EntityAccessException;
	
	void delete(Refund value) throws EntityAccessException;
	
	List<Refund> search(RefundSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
