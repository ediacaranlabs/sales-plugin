package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReportSearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface OrderReportIndexEntityAccess {

	OrderReport findById(String id) throws EntityAccessException;
	
	void save(OrderReport value) throws EntityAccessException;
	
	void update(OrderReport value) throws EntityAccessException;
	
	void delete(OrderReport value) throws EntityAccessException;
	
	List<OrderReport> search(OrderReportSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
