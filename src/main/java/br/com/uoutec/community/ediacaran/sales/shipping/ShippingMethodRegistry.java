package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.List;

import br.com.uoutec.ediacaran.core.plugins.PublicBean;
import br.com.uoutec.entity.registry.Registry;

public interface ShippingMethodRegistry extends Registry, PublicBean{

	void registerShippingMethod(ShippingMethod value) throws ShippingMethodException;
	
	void removeShippingMethod(String value) throws ShippingMethodException;
	
	ShippingMethod getShippingMethod(String value);
	
	List<ShippingMethod> getShippingMethods(ShippingRateRequest value);
	
}
