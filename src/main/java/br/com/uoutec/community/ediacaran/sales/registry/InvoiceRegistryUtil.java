package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
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
		
		if(invoices.isEmpty()) {
			return false;
		}
		
		boolean isInvoiceComplete = true;
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 loadInvoicesToCalculateUnits(invoices, null, map);
		 
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

	public static Map<String,List<Invoice>> groupByOrder(List<Invoice> list){
		Map<String,List<Invoice>> map = new HashMap<>();
		
		for(Invoice i: list) {
			
			if(i.getCancelDate() != null) {
				continue;
			}
			
			List<Invoice> l = map.get(i.getOrder());
			
			if(l == null) {
				l = new ArrayList<>();
				map.put(i.getOrder(), l);
			}
			
			l.add(i);
		}
		return map;
	}
	
	public static List<ProductRequest> setUnitsAndGetCollection(Map<String, ProductRequest> productRequestMap, Map<String, Integer> itens) throws ItemNotFoundOrderRegistryException{
		
		Map<String, ProductRequest> transientItens = new HashMap<>(productRequestMap);
		List<ProductRequest> invoiceItens = new ArrayList<>();
		
		for(Entry<String,Integer> e: itens.entrySet()) {
			ProductRequest tpr = transientItens.get(e.getKey());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(e.getKey());
			}
			
			tpr.setUnits(e.getValue().intValue());
			invoiceItens.add(tpr);
			transientItens.remove(e.getKey());
		}
		
		return invoiceItens;
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

	public static void checkInvoice(Order order, List<Invoice> actualInvoices, Invoice invoice,
			Invoice actualInvoice) throws ItemNotFoundOrderRegistryException, 
		InvalidUnitsOrderRegistryException, InvoiceRegistryException {

		checkCanceledInvoice(invoice, actualInvoice);
		checkIsCompletedInvoice(order, actualInvoices);
		checkUnits(order, actualInvoices, invoice);		
		
	}

	public static void checkCanceledInvoice(Invoice invoice, Invoice actualInvoice) throws InvoiceRegistryException {
		
		if(actualInvoice != null) {
			
			if(actualInvoice.getCancelDate() != null) {
			
				if(invoice.getCancelDate() == null || actualInvoice.getCancelDate().compareTo(invoice.getCancelDate()) != 0) {
					throw new InvoiceRegistryException("invalid cancelation data");
				}
			}
		}
		else
		if(invoice.getCancelDate() != null || invoice.getCancelJustification() != null) {
			throw new InvoiceRegistryException("cancelation data not allowed");
		}
		
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
	
	public static List<Invoice> getActualInvoices(Order order, Client client, InvoiceRegistry registry) throws InvoiceRegistryException{
		return registry.findByOrder(order.getId());
	}
	
	public static List<Invoice> getActualInvoices(Order order, Client client, InvoiceEntityAccess entityAccess) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), client);		
	}

	public static Invoice getActualInvoice(Invoice invoice, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException{
		try {
			return entityAccess.findById(invoice.getId());
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}
	
	public static Client getActualUser(Order order, Client client, ClientRegistry clientRegistry) throws OrderRegistryException, InvoiceRegistryException {
		Client actualClient;
		try{
			actualClient = clientRegistry.findClientById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}
		
		if(actualClient.getId() != client.getId()) {
			throw new InvoiceRegistryException("invalid user: " + actualClient.getId()+ " != " + client.getId());
		}
		
		return actualClient;
	}

	public static void markAsComplete(Order order, Invoice invoice, List<Invoice> invoices, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvoiceRegistryException, OrderRegistryException{
		
		List<Invoice> allInvoices = new ArrayList<>(invoices);
		
		if(!allInvoices.contains(invoice)) {
			allInvoices.add(invoice);
		}
		
		markAsComplete(order, allInvoices, orderRegistry); 
	}
	
	public static void registerEvent(Invoice invoice, Order order, String message, OrderRegistry orderRegistry) throws OrderRegistryException {
		orderRegistry.registryLog(order.getId(), message != null? message : "Criada a fatura #" + invoice.getId() );
	}
	
	public static void markAsComplete(Order order, List<Invoice> invoices, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvoiceRegistryException, OrderRegistryException{
		
		if(isCompletedInvoice(order, invoices)) {
			OrderRegistryUtil.updateStatus(order, OrderStatus.ORDER_INVOICED, orderRegistry);
			order.setCompleteInvoice(LocalDateTime.now());
		}
		else {
			OrderRegistryUtil.updateStatus(order,OrderRegistryUtil.toOrderStatus(order.getPayment().getStatus()), orderRegistry);
			order.setCompleteShipping(null);
			order.setCompleteInvoice(null);
		}
		
		try {
			orderRegistry.registerOrder(order);
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
			
	}

	public static void checkIfExistsShipping(Order order, ShippingRegistry shippingRegistry
			) throws InvoiceRegistryException {
		
		List<Shipping> list;
		try {
			list = shippingRegistry.findByOrder(order.getId());
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
		
		if(!list.isEmpty()) {
			throw new InvoiceRegistryException("found shipping for order #" + order.getId());
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
		
		checkShipping(shippings);
	}

	public static void checkShipping(List<Shipping> shippings) throws InvoiceRegistryException {
		
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
	
	public static void registerProducts(Invoice invoice, SystemUser user, Order order, ProductTypeRegistry productTypeRegistry) throws InvoiceRegistryException {
		for(ProductRequest productRequest: invoice.getItens()){
			try{
				ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
				ProductTypeHandler productTypeHandler = productType.getHandler();
				productTypeHandler.registryItem(user, order, productRequest);
			}
			catch(Throwable e){
				throw new InvoiceRegistryException(
					"falha ao processar o produto/serviço " + productRequest.getId(), e);
			}
		}
	}
	
	public static void save(Invoice invoice, Order order, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException {
		try {
			entityAccess.save(invoice);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(
				"invoice error: " + order.getId(), e);
		}
	}

	public static void update(Invoice invoice, Order order, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException {
		try {
			entityAccess.update(invoice);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new InvoiceRegistryException(
				"invoice error: " + order.getId(), e);
		}
	}
	
	public static Order getActualOrder(Order order, OrderRegistry orderRegistry) throws OrderRegistryException {
		return orderRegistry.findById(order.getId());		
	}

	public static void preventChangeInvoiceSensitiveData(Invoice invoice, Invoice actualInvoice) throws InvoiceRegistryException {
		invoice.setDate(actualInvoice.getDate());
		invoice.setCancelDate(actualInvoice.getCancelDate());
		invoice.setCancelJustification(actualInvoice.getCancelJustification());
	}
	
	public static Invoice toInvoice(Order order, Collection<ProductRequest> itens) {
		Invoice i = new Invoice();
		i.setId(null);
		i.setOwner(order.getOwner());
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(new ArrayList<ProductRequest>(itens));
		i.setCurrency(order.getCurrency());

		if(order.getTaxes() != null) {
			List<Tax> list = new ArrayList<>();
			i.setTaxes(list);
			for(Tax t: order.getTaxes()) {
				Tax nt = new Tax(t);
				nt.setId(null);
				list.add(nt);
			}
		}
		
		return i;
	}

	public static void cancelInvoices(List<Invoice> invoices, Order order, 
			String justification, LocalDateTime cancelDate, OrderRegistry orderRegistry, 
			ShippingRegistry shippingRegistry, InvoiceEntityAccess entityAccess) throws OrderRegistryException, InvoiceRegistryException, EntityAccessException {

		Order actualOrder = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		InvoiceRegistryUtil.checkShipping(actualOrder, shippingRegistry);
			
		for(Invoice i: invoices) {
			
			i.setCancelDate(cancelDate);
			i.setCancelJustification(justification);
			entityAccess.update(i);
			
			orderRegistry.registryLog(actualOrder.getId(), "canceled invoice #" + i.getId() + ": " +  justification);
			
		}

		entityAccess.flush();
			
		List<Invoice> actualInvoices = entityAccess.findByOrder(actualOrder.getId(), null);
		InvoiceRegistryUtil.markAsComplete(actualOrder, actualInvoices, orderRegistry);
		
	}

	public static void preventChangeInvoiceSaveSensitiveData(Invoice invoice) {
		invoice.setDate(LocalDateTime.now());
		invoice.setCancelDate(null);
		invoice.setCancelJustification(null);
	}
	
}
