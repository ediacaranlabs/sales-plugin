package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.persistence.EntityAccessException;

public interface InvoiceEntityAccess {

	Invoice findById(Serializable id) throws EntityAccessException;
	
	void save(Invoice value) throws EntityAccessException;
	
	void update(Invoice value) throws EntityAccessException;
	
	void delete(Invoice value) throws EntityAccessException;
	
	List<Invoice> getList(Integer first, Integer max, SystemUser user) throws EntityAccessException;
	
	List<Invoice> findByOrder(String order, SystemUser user) throws EntityAccessException;
	
	List<InvoiceResultSearch> search(InvoiceSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
