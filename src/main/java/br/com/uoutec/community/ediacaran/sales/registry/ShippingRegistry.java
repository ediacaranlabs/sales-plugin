package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;
import java.util.Map;

import br.com.uoutec.community.ediacaran.persistence.registry.CountryRegistryException;
import br.com.uoutec.community.ediacaran.sales.entity.Order;
import br.com.uoutec.community.ediacaran.sales.entity.Shipping;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingResultSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ShippingSearch;
import br.com.uoutec.community.ediacaran.user.registry.SystemUserID;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.i18n.ValidationException;

public interface ShippingRegistry extends PublicBean{

	void registerShipping(Shipping entity) throws ShippingRegistryException, OrderRegistryException, ValidationException;
	
	void removeShipping(Shipping entity) throws ShippingRegistryException;
	
	Shipping findById(String id) throws ShippingRegistryException;

	Shipping toShipping(Order order, String shippingType) throws CountryRegistryException, ShippingRegistryException, OrderRegistryException, InvoiceRegistryException;
	
	ShippingResultSearch searchShipping(ShippingSearch value) throws ShippingRegistryException;
	
	Shipping createShipping(Order order, String shippingType, Map<String, String> data, 
			Map<String, Integer> itens, String message) throws ShippingRegistryException, OrderRegistryException, InvoiceRegistryException, 
			RefundRegistryException, OrderReportRegistryException, ValidationException;

	void cancelShipping(Shipping shipping, String justification
			) throws ShippingRegistryException, OrderRegistryException, ProductTypeRegistryException, RefundRegistryException, OrderReportRegistryException, InvoiceRegistryException;

	void confirmShipping(Shipping shipping) throws ShippingRegistryException, OrderRegistryException, RefundRegistryException, OrderReportRegistryException, InvoiceRegistryException;
	
	List<Shipping> findByOrder(String id) throws ShippingRegistryException;
	
	List<Shipping> findByOrder(String id, SystemUserID userID) throws ShippingRegistryException;
	
}
