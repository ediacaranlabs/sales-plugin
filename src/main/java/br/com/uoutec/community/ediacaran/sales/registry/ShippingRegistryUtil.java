package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.ActionsPluginInstaller;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Invoice;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.OrderReport;
import br.com.uoutec.community.ediacaran.sales.entity.OrderStatus;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.entity.Refund;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.community.ediacaran.system.entity.EntityInheritanceManager;
import br.com.uoutec.community.ediacaran.system.util.DataUtil;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.persistence.EntityAccessException;

public class ShippingRegistryUtil {

	public static void checkIsCompletedShipping(Order order, Collection<Refund> refunds, Collection<Shipping> shippings) throws ShippingRegistryException, InvalidUnitsOrderRegistryException, ProductTypeRegistryException {
		
		if(isCompletedShipping(order, refunds, shippings)) {
			throw new CompletedShippingRegistryException();
		}
	}
	
	public static boolean isCompletedShipping(Order order, Collection<Refund> refunds, Collection<Shipping> shippings) throws InvalidUnitsOrderRegistryException {
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		refunds.stream()
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
		shippings.stream()
			.filter((e)->!e.isCanceled())
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
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

	public static boolean isCompletedShippingAndReceived(Order order, Collection<Refund> refunds, Collection<Shipping> shippings) throws InvalidUnitsOrderRegistryException {
		
		if(!isCompletedShipping(order, refunds, shippings)) {
			return false;
		}
		
		return isShippingReceived(order, refunds, shippings);
	}

	public static boolean isShippingReceived(Order order, Collection<Refund> refunds, Collection<Shipping> shippings) throws InvalidUnitsOrderRegistryException {
		
		for(Shipping shipping: shippings) {
			if(!shipping.isCompleted()) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void checkShippableProducts(Shipping shipping, ProductTypeRegistry productTypeRegistry) throws ProductTypeRegistryException, ShippingRegistryException {
		for(ProductRequest pr: shipping.getProducts()) {
			
			ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			
			if(!productTypeHandler.isSupportShipping(pr)) {
				throw new ShippingRegistryException("product not supported: " + pr.getSerial());
			}
			
		}
		
	}
	
	
	public static void checkUnits(Shipping shipping, Order order, List<Invoice> invoices, List<Shipping> shippings) throws ShippingRegistryException, ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		ProductRequestUtil.resetUnits(map);
		
		invoices.stream()
			.filter((e)->e.getCancelDate() == null)
			.forEach((e)->{ProductRequestUtil.addUnits(map, e.getItens());});
		
		shippings.stream()
			.filter((e)->!e.equals(shipping) && !e.isCanceled())
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
		for(ProductRequest pr: shipping.getProducts()) {
			
			ProductRequest tpr = map.get(pr.getSerial());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(pr.getSerial());
			}

			if(tpr.getUnits() - pr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException(pr.getSerial());
			}
			
		}
		
	}

	public static Shipping getActualShipping(Shipping shipping, ShippingEntityAccess entityAccess) throws ShippingRegistryException{
		try {
			return entityAccess.findById(shipping.getId());
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}
	
	public static Shipping getActualShipping(String id, ShippingEntityAccess entityAccess) throws ShippingRegistryException{
		try {
			return entityAccess.findById(id);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	public static List<Shipping> getActualShippings(Order order, ShippingRegistry registry) throws ShippingRegistryException{
		return registry.findByOrder(order.getId());
	}
	
	public static List<Shipping> getActualShippings(Order order, ShippingEntityAccess entityAccess) throws ShippingRegistryException{
		try {
			return entityAccess.findByOrder(order.getId());
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	public static List<Invoice> getActualInvoices(Order order, InvoiceRegistry invoiceRegistry) throws InvoiceRegistryException {
		return invoiceRegistry.findByOrder(order.getId());		
	}

	public static List<OrderReport> getActualReports(Order order, OrderReportRegistry orderReportRegistry) throws OrderReportRegistryException {
		return orderReportRegistry.findByOrder(order);		
	}
	
	public static List<Refund> getActualRefunds(Order order, RefundRegistry refundRegistry) throws RefundRegistryException {
		return refundRegistry.findRefundByOrder(order.getId());	
	}
	
	public static void confirmShipping(Shipping shipping, ShippingEntityAccess entityAccess) throws ShippingRegistryException {
		shipping.setReceivedDate(LocalDateTime.now());
		shipping.setClosed(true);
	}
	
	public static Client getActualClient(Order order, Client user, ClientRegistry systemUserRegistry) throws OrderRegistryException, ShippingRegistryException {
		Client actuaClient;
		try{
			actuaClient = systemUserRegistry.findClientById(order.getClient().getId());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getClient());
		}
		
		if(actuaClient.getId() != user.getId()) {
			throw new ShippingRegistryException("invalid user: " + actuaClient.getId()+ " != " + user.getId());
		}
		
		return actuaClient;
	}

	
	public static void updateStatus(Shipping entity, Order order, List<Refund> actualRefunds, List<Shipping> actualShiping, List<Invoice> actualInvoice, List<OrderReport> reports, OrderRegistry orderRegistry) throws OrderRegistryException {
		
		OrderStatus nextStatus = OrderStatus.getNextStatus(order.getStatus(), (name)->{
			switch (name) {
			case OrderStatus.PAYMENT:
				return order.getPayment();
			case OrderStatus.INVOICES:
				return actualInvoice;
			case OrderStatus.SHIPPINGS:
				if(entity != null) {
					int indexOf = actualShiping.indexOf(entity);
					if(indexOf < 0 ) {
						actualShiping.add(entity);
					}
					else {
						actualShiping.set(indexOf, entity);
					}
				}
				return actualShiping;
			case OrderStatus.REFUNDS:
				return actualRefunds;
			case OrderStatus.REPORT:
				return reports;
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
	
	public static void saveOrUpdateIndex(Shipping e, ShippingIndexEntityAccess indexEntityAccess) throws ShippingRegistryException {
		try {
			if(indexEntityAccess.findById(e.getId()) == null) {
				indexEntityAccess.save(e);
			}
			else {
				indexEntityAccess.update(e);
			}
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	public static void deleteIndex(Shipping e, ShippingIndexEntityAccess indexEntityAccess) throws ShippingRegistryException {
		try {
			indexEntityAccess.delete(e);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}
	
	public static void registerNewShippingEvent(ActionRegistry actionRegistry, Shipping shipping) {
		actionRegistry.executeAction(
				ActionsPluginInstaller.NEW_SHIPPING_REGISTERED, 
				ActionExecutorRequestBuilder.builder()
					.withParameter("shipping", shipping.getId())
				.build()
		);
	}
	
	public static Address getOrigin() throws CountryRegistryException {
		
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		CountryRegistry countryRegistry = EntityContextPlugin.getEntity(CountryRegistry.class);
		
		Address address = new Address();
		address.setFirstName(varParser.getValue("${plugins.ediacaran.system.company_name_property}"));
		address.setLastName("");
		address.setAddressLine1(varParser.getValue("${plugins.ediacaran.system.address_line1_property}"));
		address.setAddressLine2(varParser.getValue("${plugins.ediacaran.system.address_line2_property}"));
		address.setCity(varParser.getValue("${plugins.ediacaran.system.city_property}"));
		
		String isoAlpha3 = varParser.getValue("${plugins.ediacaran.system.country_property}");
		address.setCountry(countryRegistry.getCountryByIsoAlpha3(isoAlpha3));
		
		address.setRegion(varParser.getValue("${plugins.ediacaran.system.region_property}"));
		address.setZip(varParser.getValue("${plugins.ediacaran.system.zip_property}"));
		
		return address;
	}
	
	public static Address getAddress(Client client) throws CountryRegistryException {
		
		Address address = new Address();
		address.setAddressLine1(client.getAddressLine1());
		address.setAddressLine2(client.getAddressLine2());
		address.setCity(client.getCity());
		address.setCountry(client.getCountry());
		address.setFirstName(client.getFirstName());
		address.setLastName(client.getLastName());
		address.setRegion(client.getRegion());
		address.setZip(client.getZip());
		
		return address;
	}
	
	public static boolean isCanceledShipping(Invoice invoice) {
		return invoice.getCancelDate() != null;
	}

	public static Order getActualOrder(Order order, OrderRegistry orderRegistry) throws OrderRegistryException {
		return orderRegistry.findById(order.getId());		
	}

	public static Order getActualOrder(String id, OrderRegistry orderRegistry) throws OrderRegistryException {
		return orderRegistry.findById(id);		
	}
	
	public static Map<String,List<Shipping>> groupByOrder(List<Shipping> list){
		Map<String,List<Shipping>> map = new HashMap<>();
		
		for(Shipping i: list) {
			
			if(i.getCancelDate() != null) {
				continue;
			}
			
			List<Shipping> l = map.get(i.getOrder());
			
			if(l == null) {
				l = new ArrayList<>();
				map.put(i.getOrder(), l);
			}
			
			l.add(i);
		}
		return map;
	}

	public static void cancelShippings(Order order, List<Refund> refunds, List<Invoice> invoices, List<Shipping> shippings, List<OrderReport> reports, 
			String justification, LocalDateTime cancelDate, OrderRegistry orderRegistry, 
			ShippingRegistry shippingRegistry, ShippingEntityAccess entityAccess, ShippingIndexEntityAccess indexEntityAccess) throws OrderRegistryException, EntityAccessException, ShippingRegistryException, ProductTypeRegistryException {

		Order actualOrder = InvoiceRegistryUtil.getActualOrder(order, orderRegistry);
			
		OrderRegistryUtil.checkNewOrderStatus(actualOrder, OrderStatus.ORDER_INVOICED);
		
		for(Shipping i: shippings) {
			
			i.setCancelDate(cancelDate);
			i.setCancelJustification(justification);
			update(i, actualOrder, entityAccess);
			saveOrUpdateIndex(i, indexEntityAccess);
			OrderRegistryUtil.registerEvent("canceled shipping #" + i.getId() + ": " +  justification, actualOrder, orderRegistry);
			
		}

		entityAccess.flush();
			
		List<Shipping> actualShippings = getActualShippings(actualOrder, entityAccess);
		
		updateStatus(null, actualOrder, refunds, actualShippings, invoices, reports, orderRegistry);
		
	}	
	
	public static Shipping toShipping(Order order, String shippingType, Map<String,String> data, List<Shipping> shippings) throws CountryRegistryException, ShippingRegistryException {
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		if(shippings != null) {
			shippings.stream()
				.filter((e)->!e.isCanceled())
				.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		}
		
		ProductRequestUtil.removeEmptyUnits(map);
		
		Shipping i = createNewInstance(shippingType, data);
		
		i.setDate(LocalDateTime.now());
		i.setOrigin(ShippingRegistryUtil.getOrigin());
		i.setDest(new Address(order.getShippingAddress()));
		i.getDest().setId(0);
		i.setClient(order.getClient());
		i.setProducts(new ArrayList<ProductRequest>(map.values()));
		
		i.setOrder(order.getId());

		return i;
	}

	public static Shipping toShipping(Order order, String shippingType, Map<String,String> data, Map<String, Integer> itens) throws CountryRegistryException, ShippingRegistryException, ItemNotFoundOrderRegistryException {
		
		Collection<ProductRequest> list = ProductRequestUtil.createCollectionRequest(order.getItens(), itens);
		
		Shipping i = createNewInstance(shippingType, data);
		
		i.setDate(LocalDateTime.now());
		i.setOrigin(ShippingRegistryUtil.getOrigin());
		i.setDest(new Address(order.getShippingAddress()));
		i.getDest().setId(0);
		i.setClient(order.getClient());
		i.setProducts(new ArrayList<>(list));
		i.setShippingType(shippingType);
		
		i.setOrder(order.getId());

		return i;
	}
	
	public static void checkHasShippingRequest(Shipping shipping, Order order, List<Invoice> invoices, List<Shipping> shippings) throws ShippingRegistryException, ItemNotFoundOrderRegistryException, InvalidUnitsOrderRegistryException {
		
		if(shippings.contains(shipping)) {
			shippings.remove(shipping);
		}
		
		Map<String, ProductRequest> map = ProductRequestUtil.toMap(order.getItens());
		
		ProductRequestUtil.resetUnits(map);
		
		invoices.stream()
			.filter((e)->e.getCancelDate() == null)
			.forEach((e)->{ProductRequestUtil.addUnits(map, e.getItens());});
		
		shippings.stream()
			.filter((e)->!e.isCanceled())
			.forEach((e)->{ProductRequestUtil.subUnits(map, e.getProducts());});
		
		for(ProductRequest pr: shipping.getProducts()) {
			
			ProductRequest tpr = map.get(pr.getSerial());
			
			if(tpr == null) {
				throw new ItemNotFoundOrderRegistryException(pr.getSerial());
			}

			if(tpr.getUnits() - pr.getUnits() < 0) {
				throw new InvalidUnitsOrderRegistryException(pr.getSerial());
			}
			
		}
		
	}
	
	public static void save(Shipping shipping, Order order, ShippingEntityAccess entityAccess) throws ShippingRegistryException {
		try {
			entityAccess.save(shipping);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new ShippingRegistryException(
				"shipping error: " + order.getId(), e);
		}
	}

	public static void update(Shipping shipping, Order order, ShippingEntityAccess entityAccess) throws ShippingRegistryException {
		try {
			entityAccess.update(shipping);
			entityAccess.flush();
		}
		catch(Throwable e){
			throw new ShippingRegistryException(
				"shipping error: " + order.getId(), e);
		}
	}
	
	public static void preventChangeShippingSensitiveData(Shipping shipping, Shipping actualShipping) {
		shipping.setDate(actualShipping.getDate());
		shipping.setCancelDate(actualShipping.getCancelDate());
		shipping.setCancelJustification(actualShipping.getCancelJustification());
	}

	public static void preventChangeShippingSaveSensitiveData(Shipping shipping) {
		shipping.setDate(LocalDateTime.now());
		shipping.setCancelDate(null);
		shipping.setCancelJustification(null);
	}

	public static void checkAllowedCreateShipping(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedCreateShipping()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
	public static void checkAllowedUpdateShipping(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedChangeShipping()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}

	public static Shipping createNewInstance(String shippingType, Map<String,String> data) throws CountryRegistryException, ShippingRegistryException {
		Shipping i;
		
		if(shippingType == null) {
			i =  new Shipping();
		}
		else{
			EntityInheritanceManager entityInheritanceUtil = 
					EntityContextPlugin.getEntity(EntityInheritanceManager.class);
			
			try {
				i = entityInheritanceUtil.getInstance(Shipping.class, shippingType);
			}
			catch(Throwable ex) {
				throw new ShippingRegistryException(ex);
			}
			
			if(i == null){
				i = new Shipping();
			}
		}
	
		if(data != null){
			DataUtil.decode(data, i);
			i.setAddData(data);
		}
		
		return i;
	}
	
	public static void markOrderAsComplete(Shipping shipping, Order order, List<Refund> refunds, List<Shipping> shippings, List<OrderReport> reports, OrderRegistry orderRegistry) throws ShippingRegistryException {
		
		List<Shipping> allShippings = new ArrayList<>(shippings);
		
		int indexOf = allShippings.indexOf(shipping);
		if(indexOf == -1) {
			allShippings.add(shipping);
		}
		else {
			allShippings.set(indexOf, shipping);
		}
		
		try {
			if(ShippingRegistryUtil.isCompletedShippingAndReceived(order, refunds, shippings) &&
				OrderReportRegistryUtil.isCompletedOrderReport(order, reports)) {
				orderRegistry.updateStatus(order, OrderStatus.COMPLETE);
			}
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
	}
	
}
