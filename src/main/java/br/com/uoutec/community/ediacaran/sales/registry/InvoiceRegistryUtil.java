package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
import br.com.uoutec.persistence.EntityAccessException;

public class InvoiceRegistryUtil {

	public static void checkIsCompletedInvoice(Order order, Collection<Invoice> invoices
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException {
		
		if(isCompletedInvoice(order, invoices)) {
			throw new CompletedInvoiceRegistryException();
		}
	}
	
	public static boolean isCompletedInvoice(Order order, Collection<Invoice> invoices
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException {
		boolean isInvoiceComplete = true;
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 loadInvoicesToCalculateUnits(invoices, null,map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				isInvoiceComplete = false;
				break;
			}
			
		}
		
		return isInvoiceComplete;
	}
	
	public static Map<String, ProductRequest> toMap(Collection<ProductRequest> values){
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			ProductRequest tpr = new ProductRequest(pr);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		return transientItens;
	}
	
	public static void loadInvoicesToCalculateUnits(Collection<Invoice> invoices, Invoice actualInvoice, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(invoices != null) {
			for(Invoice i: invoices) {
				
				if(i.getCancelDate() != null) {
					continue;
				}
				
				if(actualInvoice != null && i.getId().equals(actualInvoice.getId())) {
					continue;
				}
				
				for(ProductRequest pr: i.getItens()) {
					ProductRequest tpr = productRequests.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}
				}
				
			}
		}
		
	}

	public static void checkInvoice(Order order, List<Invoice> actualInvoices, Invoice invoice
			) throws ItemNotFoundOrderRegistryException, 
		InvalidUnitsOrderRegistryException, CompletedInvoiceRegistryException {

		checkIsCompletedInvoice(order, actualInvoices);
		checkUnits(order, actualInvoices, invoice);		
		
	}
	
	public static void checkUnits(Order order, List<Invoice> actualInvoices, Invoice invoice
			) throws InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException {

		Map<String, ProductRequest> map = toMap(order.getItens());
		 loadInvoicesToCalculateUnits(actualInvoices, null, map);
		
		for(ProductRequest pr: invoice.getItens()) {
			
			ProductRequest tpr = map.get(pr.getSerial());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(pr.getSerial());
			}

			if(pr.getUnits() <= 0 || pr.getUnits() > tpr.getUnits()) {
				throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
			}
			
			if(tpr.getUnits() - pr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
			}
			
		}
		
	}
	
	public static List<Invoice> getActualInvoices(Order order, SystemUser user, InvoiceEntityAccess entityAccess) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), user);		
	}
	
	public static SystemUser getActualUser(Order order, SystemUser user, SystemUserRegistry systemUserRegistry) throws OrderRegistryException, InvoiceRegistryException {
		SystemUser actualUser;
		try{
			actualUser = systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}
		
		if(actualUser.getId() != user.getId()) {
			throw new InvoiceRegistryException("invalid user: " + actualUser.getId()+ " != " + user.getId());
		}
		
		return actualUser;
	}

	public static void markAsComplete(Order order, List<Invoice> invoices, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException, InvoiceRegistryException{
		
		if(isCompletedInvoice(order, invoices)) {
			order.setCompleteInvoice(LocalDateTime.now());
		}
		else {
			order.setCompleteInvoice(null);
		}
		
		try {
			orderRegistry.registerOrder(order);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
			
	}

	public static void checkPayment(Order order) throws InvoiceRegistryException {
		if(order.getPayment().getReceivedFrom() == null) {
			throw new InvoiceRegistryException("payment has not yet been made");
		}
	}
	public static void checkShipping(Order order, ShippingRegistry shippingRegistry
			) throws InvoiceRegistryException {
		
		List<Shipping> shippings;
		
		try {
			shippings = shippingRegistry.findByOrder(order.getId());
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
		
		for(Shipping shipping: shippings) {
			if(shipping.getCancelDate() == null) {
				throw new InvoiceRegistryException("exist shipping: #" + shipping.getId());
			}
		}
	}
	
	public static void checkCanceledInvoice(Invoice invoice) throws CanceledInvoiceRegistryException {
		if(isCanceledInvoice(invoice)) {
			throw new CanceledInvoiceRegistryException(invoice.getId());
		}
	}
	
	public static boolean isCanceledInvoice(Invoice invoice) {
		return invoice.getCancelDate() != null;
	}
	
}
