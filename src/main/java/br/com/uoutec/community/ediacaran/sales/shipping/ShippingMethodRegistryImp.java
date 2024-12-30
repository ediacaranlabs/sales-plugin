package br.com.uoutec.community.ediacaran.sales.shipping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;

@Singleton
public class ShippingMethodRegistryImp implements ShippingMethodRegistry {

	private Logger logger = LoggerFactory.getLogger(ShippingMethodRegistry.class);
	
	private ConcurrentMap<String, ShippingMethod> map;
	
	public ShippingMethodRegistryImp() {
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void flush() {
	}

	@Override
	public void registerShippingMethod(ShippingMethod value) throws ShippingMethodException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_METHOD.getRegisterPermission());

		ShippingMethod old = map.put(value.getId().toLowerCase(), value);
		
		if(logger.isTraceEnabled()) {
			if(old != null) {
				logger.trace("shipping method has overridden: old=" + old + ", new=" + value);
			}
			else {
				logger.trace("shipping method has added: " + value);
			}

		}
	}

	@Override
	public void removeShippingMethod(String value) throws ShippingMethodException {

		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.SHIPPING_METHOD.getRemovePermission());

		ShippingMethod old;
		
		if((old = map.remove(value)) != null) {
			if(logger.isTraceEnabled()) {
				logger.trace("shipping method has removed: " + old);
			}
		}
		
	}

	@Override
	public ShippingMethod getShippingMethod(String code) {
		return map.get(code);
	}

	@Override
	public List<ShippingMethod> getShippingMethods(ShippingRateRequest value) {
		List<ShippingMethod> result = new ArrayList<>();
		map.values().stream().forEach((e)->{
			if(e.isApplicable(value)) {
				result.add(e);
			}
		});
		return result;
	}

}
