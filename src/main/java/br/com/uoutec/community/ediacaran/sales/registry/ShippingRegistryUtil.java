package br.com.uoutec.community.ediacaran.sales.registry;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.ProductRequest;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.persistence.ShippingEntityAccess;
import br.com.uoutec.community.ediacaran.sales.shipping.ProductPackage;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistry;
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
	
	public static Client getActualClient(Order order, SystemUser user, SystemUserRegistry systemUserRegistry) throws OrderRegistryException, InvoiceRegistryException {
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
		
		return Client.toClient(actualUser);
	}

	public static void markAsComplete(Order order, List<Shipping> shippings, OrderRegistry orderRegistry
			) throws CompletedInvoiceRegistryException, InvalidUnitsOrderRegistryException, InvoiceRegistryException{
		
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
			throw new InvoiceRegistryException(ex);
		}
			
	}
	
}
