package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.community.ediacaran.sales.SalesPluginPermissions;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;

@Singleton
public class ProductTypeRegistryImp
	extends AbstractRegistry
	implements ProductTypeRegistry{

	private ConcurrentMap<String, ProductType> map;
	
	public ProductTypeRegistryImp() {
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void registerProductType(ProductType e) throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_TYPE.getRegisterPermission());
		
		map.put(e.getCode(), e);
	}

	@Override
	public void removeProductType(String code) throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_TYPE.getRemovePermission());
		
		map.remove(code);
	}

	@Override
	public List<ProductType> getProductTypes() throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_TYPE.getListPermission());
		
		return map.values().stream().collect(Collectors.toList());
	}

	@Override
	public ProductType getProductType(String code) throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(SalesPluginPermissions.PRODUCT_TYPE.getGetPermission());
		
		return map.get(code);
	}

	@Override
	public void flush() {
	}

}
