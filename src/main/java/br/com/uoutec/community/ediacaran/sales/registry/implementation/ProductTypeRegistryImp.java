package br.com.uoutec.community.ediacaran.sales.registry.implementation;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import br.com.uoutec.application.security.ContextSystemSecurityCheck;
import br.com.uoutec.application.security.RuntimeSecurityPermission;
import br.com.uoutec.community.ediacaran.sales.ProductTypeHandler;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistry;
import br.com.uoutec.community.ediacaran.sales.registry.ProductTypeRegistryException;
import br.com.uoutec.entity.registry.AbstractRegistry;

@Singleton
public class ProductTypeRegistryImp
	extends AbstractRegistry
	implements ProductTypeRegistry{

	public static final String basePermission = "app.registry.product_type.";
	
	private ConcurrentMap<String, ProductType> map;
	
	public ProductTypeRegistryImp() {
		this.map = new ConcurrentHashMap<>();
	}
	
	@Override
	public void registryProductType(ProductType e) throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "register"));
		
		map.put(e.getCode(), e);
	}

	@Override
	public void removeProductType(String code) throws ProductTypeRegistryException {
		
		ContextSystemSecurityCheck.checkPermission(
				new RuntimeSecurityPermission(basePermission + "remove"));
		
		map.remove(code);
	}

	@Override
	public List<ProductType> getProductTypes() throws ProductTypeRegistryException {
		return map.values().stream().collect(Collectors.toList());
	}

	@Override
	public ProductType getProductType(String code) throws ProductTypeRegistryException {
		return map.get(code);
	}

	@Override
	public void flush() {
	}

	@Override
	public ProductTypeHandler getProductTypeHandler(ProductType e) throws ProductTypeRegistryException {
		return null;
	}

}
