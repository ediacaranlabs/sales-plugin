package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingIndexEntityAccess;
import br.com.uoutec.community.ediacaran.sales.registry.implementation.OrderRegistryUtil;
import br.com.uoutec.community.ediacaran.system.actions.ActionExecutorRequestBuilder;
import br.com.uoutec.community.ediacaran.system.actions.ActionRegistry;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.persistence.EntityAccessException;

public class ShippingRegistryUtil {

	public static void checkIsCompletedShipping(Order order, Collection<Shipping> shippingList,
			ProductTypeRegistry productTypeRegistry) throws ShippingRegistryException, InvalidUnitsOrderRegistryException, ProductTypeRegistryException {
		
		if(isCompletedShipping(order, shippingList, productTypeRegistry)) {
			throw new CompletedShippingRegistryException();
		}
	}
	
	public static boolean isCompletedShipping(Order order, Collection<Shipping> shippingList,
			ProductTypeRegistry productTypeRegistry) throws InvalidUnitsOrderRegistryException, ProductTypeRegistryException {
		
		if(shippingList.isEmpty()) {
			return false;
		}
		
		boolean isComplete = true;
		
		 Map<String, ProductRequest> map = toMap(order.getItens(), productTypeRegistry);
		 loadShippingsToCalculateUnits(shippingList, null,map);
		 
		for(ProductRequest tpr: map.values()) {

			if(tpr.getUnits() > 0) {
				isComplete = false;
				break;
			}
			
		}
		
		/*
		for(Shipping shipping: shippingList) {
			if(shipping.getReceivedDate() == null) {
				isComplete = false;
				break;
			}
		}
		*/
		
		return isComplete;
	}

	public static boolean isCompletedShippingAndReceived(Order order, Collection<Shipping> shippingList,
			ProductTypeRegistry productTypeRegistry) throws InvalidUnitsOrderRegistryException, ProductTypeRegistryException {
		
		if(!isCompletedShipping(order, shippingList, productTypeRegistry)) {
			return false;
		}
		
		for(Shipping shipping: shippingList) {
			if(shipping.getCancelDate() == null && shipping.getReceivedDate() == null) {
				return false;
			}
		}
		
		
		return true;
	}
	
	public static Map<String, ProductRequest> toMap(Collection<ProductRequest> values, 
			ProductTypeRegistry productTypeRegistry) throws ProductTypeRegistryException{
		
		Map<String, ProductRequest> transientItens = new HashMap<>();
		
		for(ProductRequest pr: values) {
			
			ProductType productType = productTypeRegistry.getProductType(pr.getProduct().getProductType());
			ProductTypeHandler productTypeHandler = productType.getHandler();
			
			if(!productTypeHandler.isSupportShipping(pr)) {
				continue;
			}
			
			ProductRequest tpr = new ProductRequest(pr);
			//tpr.setId(null);
			transientItens.put(pr.getSerial(), tpr);
		}
		
		return transientItens;
	}
	
	public static void loadShippingsToCalculateUnits(Collection<Shipping> shippingList, Shipping actualShipping, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(shippingList != null) {
			for(Shipping i: shippingList) {
				
				if(i.getCancelDate() != null) {
					continue;
				}
				
				if(actualShipping != null && i.getId().equals(actualShipping.getId())) {
					continue;
				}

				
				for(ProductRequest pr: i.getProducts()) {
					ProductRequest tpr = productRequests.get(pr.getSerial());
					
					tpr.setUnits(tpr.getUnits() - pr.getUnits());
					
					if(tpr.getUnits() < 0) {
						throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
					}
				}
				
			}
		}
		
	}

	public static void checkShipping(Order order, List<Shipping> actualShippings, Shipping shipping, 
			ProductTypeRegistry productTypeRegistry) throws OrderRegistryException, ShippingRegistryException, ProductTypeRegistryException {

		checkShippableProducts(shipping, productTypeRegistry);
		checkIsCompletedShipping(order, actualShippings, productTypeRegistry);
		checkUnits(order, actualShippings, shipping, productTypeRegistry);		
		
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
	
	public static void checkUnits(Order order, List<Shipping> actualShippings, Shipping shipping, 
			ProductTypeRegistry productTypeRegistry) throws OrderRegistryException, ProductTypeRegistryException {

		Map<String, ProductRequest> map = toMap(order.getItens(), productTypeRegistry);
		
		loadShippingsToCalculateUnits(actualShippings, null, map);

		for(ProductRequest pr: shipping.getProducts()) {
			
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

	public static Shipping getActualShipping(String id, ShippingEntityAccess entityAccess) throws ShippingRegistryException{
		try {
			return entityAccess.findById(id);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}

	public static List<Shipping> getActualShippings(Order order, Client client, ShippingRegistry registry) throws ShippingRegistryException{
		return registry.findByOrder(order.getId());
	}
	
	public static List<Shipping> getActualShippings(Order order, Client client, ShippingEntityAccess entityAccess) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), client);		
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

	public static void updateOrderStatus(Order actualOrder, Client actualClient, Shipping actualShipping, OrderReportRegistry orderReportRegistry, OrderRegistry orderRegistry, 
			ProductTypeRegistry productTypeRegistry, ShippingEntityAccess entityAccess) throws ShippingRegistryException {
		
		List<Shipping> shippingList;
		
		try {
			shippingList = ShippingRegistryUtil.getActualShippings(actualOrder, actualClient, entityAccess);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
		shippingList.add(actualShipping);

		List<OrderReport> orderReportList;
		
		try {
			orderReportList = OrderReportRegistryUtil.findByOrder(actualOrder, orderReportRegistry);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
		try {
			if(ShippingRegistryUtil.isCompletedShippingAndReceived(actualOrder, shippingList.stream().collect(Collectors.toSet()), productTypeRegistry) &&
				OrderReportRegistryUtil.isCompletedOrderReport(actualOrder, orderReportList)) {
				orderRegistry.updateStatus(actualOrder, OrderStatus.COMPLETE);
			}
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		
	}
	
	public static void markAsComplete(Order order, Shipping shipping, List<Shipping> shippings, 
			OrderRegistry orderRegistry, ProductTypeRegistry productTypeRegistry) throws ShippingRegistryException, ProductTypeRegistryException, OrderRegistryException{
		
		List<Shipping> allShippings = new ArrayList<>(shippings);
		
		if(!allShippings.contains(shipping)) {
			allShippings.add(shipping);
		}
		
		markAsComplete(order, allShippings, orderRegistry, productTypeRegistry); 
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
	
	public static void markAsComplete(Order order, List<Shipping> shippings, OrderRegistry orderRegistry,
			ProductTypeRegistry productTypeRegistry) throws ShippingRegistryException, ProductTypeRegistryException, OrderRegistryException{
		
		if(isCompletedShipping(order, shippings, productTypeRegistry)) {
			OrderRegistryUtil.updateStatus(order, OrderStatus.ORDER_SHIPPED, orderRegistry);
			//order.setCompleteShipping(LocalDateTime.now());
		}
		else {
			OrderRegistryUtil.updateStatus(order, OrderStatus.ORDER_INVOICED, orderRegistry);
			//order.setCompleteShipping(null);
		}
		
		/*
		try {
			orderRegistry.registerOrder(order);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
		*/
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

	public static void cancelShippings(List<Shipping> shippings, Order order, 
			String justification, LocalDateTime cancelDate, OrderRegistry orderRegistry, 
			ShippingRegistry shippingRegistry, ShippingEntityAccess entityAccess, ShippingIndexEntityAccess indexEntityAccess,
			ProductTypeRegistry productTypeRegistry) throws OrderRegistryException, EntityAccessException, ShippingRegistryException, ProductTypeRegistryException {

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
			
		List<Shipping> actualShippings = getActualShippings(actualOrder, null, entityAccess);
		markAsComplete(actualOrder, actualShippings, orderRegistry, productTypeRegistry);
		
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
	
	public static Shipping getActualShipping(Shipping shipping, ShippingEntityAccess entityAccess) throws ShippingRegistryException{
		try {
			return entityAccess.findById(shipping.getId());
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
	}
	
	public static Shipping toShipping(Order order, Collection<ProductRequest> itens) throws CountryRegistryException {
		Shipping i = new Shipping();
		i.setAddData(new HashMap<>());
		i.setDate(LocalDateTime.now());
		i.setOrigin(ShippingRegistryUtil.getOrigin());
		i.setDest(new Address(order.getShippingAddress()));
		i.getDest().setId(0);
		i.setClient(order.getClient());
		i.setProducts(new ArrayList<ProductRequest>(itens));
		
		i.setOrder(order.getId());
		i.setShippingType(null);

		return i;
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
		if(!order.getStatus().isAllowedCreateInvoice()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
	public static void checkAllowedUpdateShipping(Order order) throws OrderStatusNotAllowedRegistryException {
		if(!order.getStatus().isAllowedChangeInvoice()) {
			throw new OrderStatusNotAllowedRegistryException("invalid status #" + order.getStatus());
		}
	}
	
}
