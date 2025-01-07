package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Client;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingsResultSearch;
import br.com.uoutec.community.ediacaran.user.entity.SystemUser;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ShippingRegistry extends PublicBean{

	void registerShipping(Shipping entity) throws ShippingRegistryException;
	
	void removeShipping(Shipping entity) throws ShippingRegistryException;
	
	Shipping findById(String id) throws ShippingRegistryException;

	Shipping findById(String id, SystemUserID userID) throws ShippingRegistryException;
	
	Shipping findById(String id, Client client) throws ShippingRegistryException;
	
	Shipping toShipping(Order order
			) throws InvalidUnitsOrderRegistryException, CountryRegistryException, OrderNotFoundRegistryException;
	
	ShippingsResultSearch searchShipping(ShippingSearch value) throws ShippingRegistryException;
	
	Shipping createShipping(Order order, SystemUserID userID, Map<String, Integer> itens, String message) 
			throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
			UnmodifiedOrderStatusRegistryException, ClientRegistryException, ShippingRegistryException;

	Shipping createShipping(Order order, Client client, Map<String, Integer> itens, String message) 
			throws ShippingRegistryException;
	
	Shipping createShipping(Order orderID, Map<String, Integer> itens, String message) 
		throws OrderRegistryException, OrderStatusNotAllowedRegistryException,
		UnmodifiedOrderStatusRegistryException, ClientRegistryException, ShippingRegistryException;

	void cancelShipping(Shipping shipping, String justification) throws ShippingRegistryException;
	
	void cancelShipping(Shipping shipping, SystemUserID userID, String justification) throws ShippingRegistryException;
	
	void cancelShipping(Shipping shipping, SystemUser systemUser, String justification) throws ShippingRegistryException;
	
	List<Shipping> findByOrder(String id) throws ShippingRegistryException;
	
	List<Shipping> findByOrder(String id, SystemUserID userID) throws ShippingRegistryException;
	
}
