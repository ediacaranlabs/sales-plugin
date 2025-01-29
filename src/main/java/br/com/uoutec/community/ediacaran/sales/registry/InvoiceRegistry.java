package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.InvoiceSearch;
import br.com.uoutec.community.ediacaran.sales.entity.InvoicesResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.RegistryException;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.persistence.EntityAccessException;

public interface InvoiceRegistry extends PublicBean{

	void registerInvoice(Invoice entity) throws ValidationException, RegistryException, EntityAccessException, ProductTypeHandlerException;
	
	void removeInvoice(Invoice entity) throws InvoiceRegistryException, ShippingRegistryException;
	
	Invoice findById(String id) throws InvoiceRegistryException;

	Invoice findById(String id, SystemUserID userID) throws InvoiceRegistryException, SystemUserRegistryException;
	
	Invoice findById(String id, SystemUser systemUser) throws InvoiceRegistryException;
	
	Invoice toInvoice(Order order)  throws OrderRegistryException, PersistenceInvoiceRegistryException;
	
	InvoicesResultSearch searchInvoice(InvoiceSearch value) throws InvoiceRegistryException;
	
	void cancelInvoice(Invoice invoice, String justification) throws InvoiceRegistryException, OrderRegistryException, ShippingRegistryException;
	
	void cancelInvoices(Order order, String justification) throws InvoiceRegistryException, OrderRegistryException, ShippingRegistryException;

	Invoice createInvoice(Order orderID, Map<String, Integer> itens, String message) throws RegistryException, ProductTypeHandlerException;

	List<Invoice> findByOrder(String id) throws InvoiceRegistryException;
	
	List<Invoice> findByOrder(String id, SystemUserID userID) throws InvoiceRegistryException, SystemUserRegistryException;
	
}
