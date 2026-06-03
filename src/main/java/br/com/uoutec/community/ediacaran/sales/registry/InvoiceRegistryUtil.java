package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.Tax;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.InvoiceIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.i18n.ValidationException;
import br.com.uoutec.i18n.ValidatorBean;
import br.com.uoutec.persistence.EntityAccessException;

public class InvoiceRegistryUtil {

	public static void checkIsCompletedInvoice(Order order, Collection<Refund> refunds, Collection<Invoice> invoices
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException {
		
		if(isCompletedInvoice(order, refunds, invoices)) {
			throw new CompletedInvoiceRegistryException();
		}
		
	}

	public static void validateInvoice(Invoice e, Class<?> ... groups) throws ValidationException{
		ValidatorBean.validate(e, groups);
	}
	
	public static boolean isCompletedInvoice(Order order, Collection<Refund> refunds, Collection<Invoice> invoices
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException {
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		refunds.stream()
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
		invoices.stream()
			.filter((e)->e.getCancelDate() == null)
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getItens());});
		
		for(ProductRequest pr: order.getItens()) {
			
			ProductRequest tpr = map.get(pr.getSerial());

			if(tpr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException("negative unit: " + tpr.getSerial());
			}
			
			if(tpr.getUnits() > 0) {
				return false;
			}
			
		}
		
		return true;
	}
	
	public static Map<String, ProductRequest> toMap(Collection<ProductRequest> values){
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			ProductRequest tpr = new ProductRequest(pr);
			//tpr.setId(null);
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
	
	public static void checkAllowedCreateInvoice(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedCreateInvoice()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
	public static void checkAllowedUpdateInvoice(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedChangeInvoice()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
	public static void checkCanceledInvoice(Invoice invoice) throws CanceledInvoiceRegistryException {
		if(isCanceledInvoice(invoice)) {
			throw new CanceledInvoiceRegistryException(invoice.getId());
		}
	}
	
	public static boolean isCanceledInvoice(Invoice invoice) {
		return invoice.getCancelDate() != null || invoice.getCancelJustification() != null;
	}
		
	public static void checkCanceledInvoiceDate(Invoice invoice, Invoice actualInvoice) throws InvoiceRegistryException {
		
		if(actualInvoice.getCancelDate() != null) {
		
			if(invoice.getCancelDate() == null || actualInvoice.getCancelDate().compareTo(invoice.getCancelDate()) != 0) {
				throw new InvoiceRegistryException("invalid cancelation data");
			}
			
		}
			
	}
	
	public static void checkUnits(Invoice invoice, Order order, List<Refund> refunds, List<Invoice> invoices
			) throws InvalidUnitsOrderRegistryException, ItemNotFoundOrderRegistryException {

		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		refunds.stream()
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
		invoices.stream()
			.filter((e)->!e.equals(invoice) && e.getCancelDate() == null)
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getItens());});
		
		for(ProductRequest pr: invoice.getItens()) {
			
			ProductRequest tpr = map.get(pr.getSerial());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(pr.getSerial());
			}

			if(tpr.getUnits() - pr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException(pr.getSerial());
			}
			
		}
		
	}
	
	public static List<Invoice> getActualInvoices(Order order, Client client, InvoiceRegistry registry) throws InvoiceRegistryException{
		return registry.findByOrder(order.getId());
	}

	public static List<Refund> getActualRefunds(Order order, RefundRegistry registry) throws RefundRegistryException {
		return registry.findRefundByOrder(order.getId());
	}

	public static List<Shipping> getActualShippings(Order order, ShippingRegistry registry) throws ShippingRegistryException {
		return registry.findByOrder(order.getId());
	}
	
	public static List<Invoice> getActualInvoices(Order order, Client client, InvoiceEntityAccess entityAccess) throws PersistenceInvoiceRegistryException{
		try {
			return entityAccess.findByOrder(order.getId());
		}
		catch(EntityAccessException e) {
			throw new PersistenceInvoiceRegistryException(e);
		}
	}

	public static Invoice getActualInvoice(Invoice invoice, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException{
		try {
			return entityAccess.findById(invoice.getId());
		}
		catch(Throwable ex) {
			throw new InvoiceRegistryException(ex);
		}
	}
	
	public static void registerNewInvoiceEvent(ActionRegistry actionRegistry, Invoice invoice) {
		actionRegistry.executeAction(
				ActionsPluginInstaller.NEW_INVOICE_REGISTERED, 
				ActionExecutorRequestBuilder.builder()
					.withParameter("invoice", invoice.getId())
				.build()
		);
	}
	
	public static Client getActualUser(Order order, ClientRegistry clientRegistry) throws OrderRegistryException, InvoiceRegistryException {
		Client actualClient;
		try{
			actualClient = clientRegistry.findClientById(order.getClient().getId());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getClient());
		}
		
		if(actualClient.getId() != order.getClient().getId()) {
			throw new InvoiceRegistryException("invalid user: " + actualClient.getId()+ " != " + order.getClient().getId());
		}
		
		return actualClient;
	}

	public static void updateStatus(Invoice entity, Order order, List<Refund> actualRefunds, List<Shipping> actualShiping, List<Invoice> actualInvoice, OrderRegistry orderRegistry) throws OrderRegistryException {
		
		OrderStatus nextStatus = OrderStatus.getNextStatus(order.getStatus(), (name)->{
			switch (name) {
			case OrderStatus.PAYMENT:
				return order.getPayment();
			case OrderStatus.INVOICES:
				if(entity != null) {
					int indexOf = actualInvoice.indexOf(entity);
					if(indexOf < 0 ) {
						actualInvoice.add(entity);
					}
					else {
						actualInvoice.set(indexOf, entity);
					}
				}
				return actualInvoice;
			case OrderStatus.SHIPPINGS:
				return actualShiping;
			case OrderStatus.REFUNDS:
				return actualRefunds;
			case OrderStatus.ORDER:
				return order;
			}
			return null;
		});
		
		if(nextStatus != null) {
			updateOrderStatus(order, nextStatus, orderRegistry);
		}
		
	}
	
	public static void updateOrderStatus(Order actualOrder, OrderStatus nextStatus, OrderRegistry orderRegistry) throws OrderRegistryException {
		orderRegistry.updateStatus(actualOrder, nextStatus);
	}
	
	/*
	public static void markAsComplete(Order order, Collection<Refund> refunds, List<Invoice> invoices, Invoice invoice, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvoiceRegistryException, OrderRegistryException{
		
		List<Invoice> allInvoices = new ArrayList<>(invoices);
		
		int indexOf = allInvoices.indexOf(invoice);
		if(indexOf == -1) {
			allInvoices.add(invoice);
		}
		else {
			allInvoices.set(indexOf, invoice);
		}
		
		markAsComplete(order, refunds, allInvoices, orderRegistry); 
	}
	*/
	
	/*
	public static void markAsComplete(Order order, Collection<Refund> refunds, Collection<Invoice> invoices, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvoiceRegistryException, OrderRegistryException{
		
		if(isCompletedInvoice(order, refunds, invoices)) {
			OrderRegistryUtil.updateStatus(order, OrderStatus.ORDER_INVOICED, orderRegistry);
			//order.setCompleteInvoice(LocalDateTime.now());
		}
		else {
			OrderRegistryUtil.updateStatus(order,OrderRegistryUtil.toOrderStatus(order.getPayment().getStatus()), orderRegistry);
			//order.setCompleteShipping(null);
			//order.setCompleteInvoice(null);
		}
		
	}
   */
	
	public static void checkIfExistsShipping(Order order, ShippingRegistry shippingRegistry
			) throws InvoiceRegistryException, ShippingRegistryException {
		
		List<Shipping> list = shippingRegistry.findByOrder(order.getId());
		
		if(!list.isEmpty()) {
			throw new InvoiceRegistryException("found shipping for order #" + order.getId());
		}
	}
	
	public static void checkShipping(Order order, ShippingRegistry shippingRegistry
			) throws InvoiceRegistryException, ShippingRegistryException {
		
		List<Shipping> shippings = shippingRegistry.findByOrder(order.getId());
		checkShipping(shippings);
	}

	public static void checkShipping(List<Shipping> shippings) throws InvoiceRegistryException {
		
		for(Shipping shipping: shippings) {
			if(shipping.getCancelDate() == null) {
				throw new InvoiceRegistryException("exist shipping: #" + shipping.getId());
			}
		}
	}
	
	public static void registerProducts(Invoice invoice, SystemUser user, Order order, ProductTypeRegistry productTypeRegistry) throws InvoiceRegistryException, ProductTypeRegistryException, ProductTypeHandlerException {
		for(ProductRequest productRequest: invoice.getItens()){
			ProductType productType = productTypeRegistry.getProductType(productRequest.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			productTypeHandler.registryItem(user, order, productRequest);
		}
	}

	public static void saveOrUpdateIndex(Invoice invoice, InvoiceIndexEntityAccess entityAccess) throws InvoiceRegistryException {
		try {
			entityAccess.save(invoice);
			
			if(entityAccess.findById(invoice.getId()) != null) {
				entityAccess.update(invoice);
			}
			else {
				entityAccess.save(invoice);
			}
			
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new PersistenceInvoiceRegistryException(
				"invoice error: " + invoice.getOrder(), e);
		}
	}
	
	public static void save(Invoice invoice, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException {
		try {
			entityAccess.save(invoice);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new PersistenceInvoiceRegistryException(
				"invoice error: " + invoice.getOrder(), e);
		}
	}

	public static void update(Invoice invoice, InvoiceEntityAccess entityAccess) throws InvoiceRegistryException {
		try {
			entityAccess.update(invoice);
			
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new PersistenceInvoiceRegistryException(
				"invoice error: " + invoice.getOrder(), e);
		}
	}
	
	public static final void checkActualStatus(Order order) {
		
	}
	
	public static Order getActualOrder(Order order, OrderRegistry orderRegistry) throws OrderRegistryException {
		return orderRegistry.findById(order.getId());		
	}

	public static void preventChangeInvoiceSensitiveData(Invoice invoice, Invoice actualInvoice) throws InvoiceRegistryException {
		invoice.setDate(actualInvoice.getDate());
		invoice.setCancelDate(actualInvoice.getCancelDate());
		invoice.setCancelJustification(actualInvoice.getCancelJustification());
	}
	
	public static Invoice toInvoice(Order order, List<Invoice> invoices) throws ItemNotFoundOrderRegistryException {
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		if(invoices != null) {
			invoices.stream()
				.filter((e)->e.getCancelDate() == null)
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getItens());});
		}
		
		ProductRequestUtil.removeEmptyUnits(map);
		
		Invoice i = new Invoice();
		i.setId(null);
		i.setClient(order.getClient());
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(new ArrayList<>(map.values()));
		i.setCurrency(order.getCurrency());

		if(order.getTaxes() != null) {
			List<Tax> taxes = new ArrayList<>();
			i.setTaxes(taxes);
			for(Tax t: order.getTaxes()) {
				Tax nt = new Tax(t);
				nt.setId(null);
				taxes.add(nt);
			}
		}
		
		return i;
	}

	public static Invoice toInvoice(Order order, Map<String, Integer> itens) throws ItemNotFoundOrderRegistryException {
		
		Collection<ProductRequest> list = ProductRequestUtil.createCollectionRequest(order.getItens(), itens);
		
		Invoice i = new Invoice();
		i.setId(null);
		i.setClient(order.getClient());
		i.setDate(LocalDateTime.now());
		i.setOrder(order.getId());
		i.setItens(new ArrayList<>(list));
		i.setCurrency(order.getCurrency());

		if(order.getTaxes() != null) {
			List<Tax> taxes = new ArrayList<>();
			i.setTaxes(taxes);
			for(Tax t: order.getTaxes()) {
				Tax nt = new Tax(t);
				nt.setId(null);
				taxes.add(nt);
			}
		}
		
		return i;
	}
	
	public static void cancelInvoices(Order order, List<Refund> refunds, List<Invoice> invoices, List<Shipping> shippings,
			String justification, LocalDateTime cancelDate, OrderRegistry orderRegistry, 
			ShippingRegistry shippingRegistry, InvoiceEntityAccess entityAccess) throws OrderRegistryException, InvoiceRegistryException, ShippingRegistryException {

		Order actualOrder = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
		
		OrderRegistryUtil.checkNewOrderStatus(actualOrder, OrderStatus.PAYMENT_RECEIVED);
		InvoiceRegistryUtil.checkShipping(actualOrder, shippingRegistry);
			
		for(Invoice i: invoices) {
			
			i.setCancelDate(cancelDate);
			i.setCancelJustification(justification);
			
			update(i, entityAccess);
			
			orderRegistry.registryLog(actualOrder, "canceled invoice #" + i.getId() + ": " +  justification);
			
		}

		entityAccess.flush();
			
		updateStatus(null, actualOrder, refunds, shippings, invoices, orderRegistry);
		
	}

	public static void preventChangeInvoiceSaveSensitiveData(Invoice invoice) {
		invoice.setDate(LocalDateTime.now());
		invoice.setCancelDate(null);
		invoice.setCancelJustification(null);
	}
	
}
