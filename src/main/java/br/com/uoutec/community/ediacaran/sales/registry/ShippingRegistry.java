package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserRegistryException;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ShippingRegistry extends PublicBean{

	void registerShipping(Shipping entity) throws InvoiceRegistryException;
	
	void removeShipping(Shipping entity) throws InvoiceRegistryException;
	
	Shipping findById(String id) throws InvoiceRegistryException;

	Shipping findById(String id, SystemUserID userID) throws InvoiceRegistryException;
	
	Shipping findById(String id, Client client) throws InvoiceRegistryException;
	
	Shipping toShipping(Order order
			) throws OrderNotFoundRegistryException, ItemNotFoundOrderRegistryException, 
				InvalidUnitsOrderRegistryException;
	
	List<ShippingResultSearch> searchShipping(ShippingSearch value) throws InvoiceRegistryException;
	
	Shipping createShipping(Order order, SystemUserID userID, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, SystemUserRegistryException, InvoiceRegistryException;

	Shipping createShipping(Order order, SystemUser systemUser, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, InvoiceRegistryException;
	
	Shipping createShipping(Order orderID, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, SystemUserRegistryException, InvoiceRegistryException;

	List<Shipping> findByOrder(String id) throws InvoiceRegistryException;
	
	List<Shipping> findByOrder(String id, SystemUserID userID) throws InvoiceRegistryException;
	
}
