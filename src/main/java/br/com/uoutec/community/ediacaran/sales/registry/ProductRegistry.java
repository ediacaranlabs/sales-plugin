package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;

public interface ProductRegistry {

	void registryServicePlan(Product entity) throws ProductRegistryException;
	
	void removeServicePlan(Product entity) throws ProductRegistryException;
	
	Product findById(int id) throws ProductRegistryException;
	
	List<Product> getProductByType(ProductType serviceType) throws ProductRegistryException;
	
}
