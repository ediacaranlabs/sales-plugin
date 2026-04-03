package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.OrderReportMessage;
import br.com.uoutec.persistence.EntityAccessException;

public interface OrderReportMessageEntityAccess {

	OrderReportMessage findById(String id) throws EntityAccessException;
	
	void save(OrderReportMessage value) throws EntityAccessException;
	
	void update(OrderReportMessage value) throws EntityAccessException;
	
	void delete(OrderReportMessage value) throws EntityAccessException;
	
	List<OrderReportMessage> search(String id, String productRequest, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
