package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface InvoiceRegistry extends PublicBean{

	void registerInvoice(Invoice entity) throws InvoiceRegistryException;
	
	void removeInvoice(Invoice entity) throws InvoiceRegistryException;
	
	Invoice findById(String id) throws InvoiceRegistryException;

	Invoice createInvoice(String orderID, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException;

	List<Invoice> findByOrder(String id) throws InvoiceRegistryException;
	
	List<Invoice> getInvoices(Integer first, Integer max) throws InvoiceRegistryException;
	
	
}
