package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductType;

public interface ProductTypeRegistry {

	public static final String PRODUCTTYPE_TOKEN_TYPE = "PRODUCT_ACCESS";

	public static final String PRODUCTTYPE_TOKEN_VAR  = "PRODUCT_TYPE";
	
	void registryProductType(ProductType e) throws ProductTypeRegistryException;

	void removeProductType(ProductType e) throws ProductTypeRegistryException;
	
	List<ProductType> getProductTypes(Integer start, 
			Integer len) throws ProductTypeRegistryException;
	
	ProductType getProductTypeById(int id) throws ProductTypeRegistryException;

	ProductType getProductTypeByCode(String code) throws ProductTypeRegistryException;
	
}
