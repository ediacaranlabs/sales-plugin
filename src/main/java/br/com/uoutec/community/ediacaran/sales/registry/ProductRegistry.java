package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;

public interface ProductRegistry {

	void registerProduct(Product entity) throws ProductRegistryException;
	
	void removeProduct(Product entity) throws ProductRegistryException;
	
	Product findProductById(int id) throws ProductRegistryException;
	
	List<Product> getProductByType(ProductType serviceType) throws ProductRegistryException;
	
	ProductSearchResult search(ProductSearch value)	throws ProductRegistryException;
	
}
