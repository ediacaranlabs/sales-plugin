package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.ediacaran.core.plugins.PublicBean;

public interface ProductTypeRegistry extends PublicBean{

	public static final String PRODUCTTYPE_TOKEN_TYPE = "PRODUCT_ACCESS";

	public static final String PRODUCTTYPE_TOKEN_VAR  = "PRODUCT_TYPE";
	
	void registerProductType(ProductType e) throws ProductTypeRegistryException;

	void removeProductType(String code) throws ProductTypeRegistryException;
	
	List<ProductType> getProductTypes() throws ProductTypeRegistryException;
	
	ProductType getProductType(String code) throws ProductTypeRegistryException;
	
}
