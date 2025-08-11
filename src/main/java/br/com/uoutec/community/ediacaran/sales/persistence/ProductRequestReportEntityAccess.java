package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductRequestReport;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductRequestReportEntityAccess {

	ProductRequestReport findById(String id) throws EntityAccessException;

	void save(ProductRequestReport value) throws EntityAccessException;
	
	void update(ProductRequestReport value) throws EntityAccessException;
	
	void delete(ProductRequestReport value) throws EntityAccessException;
	
	List<ProductRequestReport> getByOrderReport(String id, Integer firstResult, Integer max) throws EntityAccessException;
	
	void flush();
	
}
