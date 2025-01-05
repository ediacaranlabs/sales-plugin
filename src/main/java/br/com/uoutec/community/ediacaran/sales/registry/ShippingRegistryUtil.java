package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistry;
import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Address;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;
import br.com.uoutec.ediacaran.core.VarParser;
import br.com.uoutec.ediacaran.core.plugins.EntityContextPlugin;
import br.com.uoutec.persistence.EntityAccessException;

public class ShippingRegistryUtil {

	public static void checkIsCompletedShipping(Order order, Collection<Shipping> shippingList
			) throws ShippingRegistryException, InvalidUnitsOrderRegistryException {
		
		if(isCompletedShipping(order, shippingList)) {
			throw new CompletedShippingRegistryException();
		}
	}
	
	public static boolean isCompletedShipping(Order order, Collection<Shipping> shippingList
			) throws InvalidUnitsOrderRegistryException {
		boolean isInvoiceComplete = true;
		
		 Map<String, ProductRequest> map = toMap(order.getItens());
		 loadShippingsToCalculateUnits(shippingList, null,map);
		 
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
	
	public static void loadShippingsToCalculateUnits(Collection<Shipping> shippingList, Shipping actualShipping, 
			Map<String, ProductRequest> productRequests) throws InvalidUnitsOrderRegistryException {
		
		if(shippingList != null) {
			for(Shipping i: shippingList) {
				
				if(actualShipping != null && i.getId().equals(actualShipping.getId())) {
					continue;
				}
				
				for(ProductPackage pp: i.getItens()) {
					
					for(ProductRequest pr: pp.getProducts()) {
						ProductRequest tpr = productRequests.get(pr.getSerial());
						
						tpr.setUnits(tpr.getUnits() - pr.getUnits());
						
						if(tpr.getUnits() < 0) {
							throw new InvalidUnitsOrderRegistryException(tpr.getSerial());
						}
					}
					
				}
				
				
			}
		}
		
	}

	public static void checkShipping(Order order, List<Shipping> actualShippings, Shipping shipping
			) throws OrderRegistryException, ShippingRegistryException {

		checkIsCompletedShipping(order, actualShippings);
		checkUnits(order, actualShippings, shipping);		
		
	}
	
	public static void checkUnits(Order order, List<Shipping> actualShippings, Shipping shipping
			) throws OrderRegistryException {

		Map<String, ProductRequest> map = toMap(order.getItens());
		
		loadShippingsToCalculateUnits(actualShippings, null, map);
		
		for(ProductPackage pp: shipping.getItens()) {

			for(ProductRequest pr: pp.getProducts()) {
				
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
		
	}
	
	public static List<Shipping> getActualShippings(Order order, Client client, ShippingEntityAccess entityAccess) throws EntityAccessException{
		return entityAccess.findByOrder(order.getId(), client);		
	}
	
	public static Client getActualClient(Order order, Client user, ClientRegistry systemUserRegistry) throws OrderRegistryException, ShippingRegistryException {
		Client actuaClient;
		try{
			actuaClient = systemUserRegistry.findById(order.getOwner());
		}
		catch(Throwable e){
			throw new OrderRegistryException("usuário não encontrado: " + order.getOwner());
		}
		
		if(actuaClient.getId() != user.getId()) {
			throw new ShippingRegistryException("invalid user: " + actuaClient.getId()+ " != " + user.getId());
		}
		
		return actuaClient;
	}

	public static void markAsComplete(Order order, List<Shipping> shippings, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException, ShippingRegistryException{
		
		if(isCompletedShipping(order, shippings)) {
			order.setCompleteShipping(LocalDateTime.now());
		}
		else {
			order.setCompleteShipping(null);
		}
		
		try {
			orderRegistry.registerOrder(order);
		}
		catch(Throwable ex) {
			throw new ShippingRegistryException(ex);
		}
			
	}
	
	public static Address getOrigin() throws CountryRegistryException {
		
		VarParser varParser = EntityContextPlugin.getEntity(VarParser.class);
		CountryRegistry countryRegistry = EntityContextPlugin.getEntity(CountryRegistry.class);
		
		Address address = new Address();
		address.setAddressLine1(varParser.getValue("${plugins.ediacaran.system.address_line1_property}"));
		address.setAddressLine2(varParser.getValue("${plugins.ediacaran.system.address_line2_property}"));
		address.setCity(varParser.getValue("${plugins.ediacaran.system.city_property}"));
		
		String isoAlpha3 = varParser.getValue("${plugins.ediacaran.system.country_property}");
		address.setCountry(countryRegistry.getCountryByIsoAlpha3(isoAlpha3));
		
		address.setRegion(varParser.getValue("${plugins.ediacaran.system.region_property}"));
		address.setZip(varParser.getValue("${plugins.ediacaran.system.zip_property}"));
		
		return address;
	}
}
