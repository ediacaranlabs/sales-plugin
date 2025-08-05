package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface InvoiceIndexEntityAccess {

	Invoice findById(Serializable id) throws EntityAccessException;
	
	void save(Invoice value) throws EntityAccessException;
	
	void update(Invoice value) throws EntityAccessException;
	
	void delete(Invoice value) throws EntityAccessException;
	
	List<Invoice> search(InvoiceSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
