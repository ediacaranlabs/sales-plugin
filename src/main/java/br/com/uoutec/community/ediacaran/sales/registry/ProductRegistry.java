package br.com.uoutec.community.ediacaran.sales.registry;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearchResult;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;

public interface ProductRegistry {

	void registerProduct(Product entity) throws ProductRegistryException;
	
	void removeProduct(Product entity) throws ProductRegistryException;
	
	Product findProductById(int id) throws ProductRegistryException;
	
	List<Product> getProductByType(ProductType serviceType) throws ProductRegistryException;
	
	ProductSearchResult search(ProductSearch value)	throws ProductRegistryException;

	void registerProductImages(List<ProductImage> images, Product entity) throws ProductRegistryException;

	void removeProductImages(List<ProductImage> images, Product entity) throws ProductRegistryException;
	
	public ProductImage getImagesByID(String id) throws ProductRegistryException;
	
	List<ProductImage> getImagesByProduct(Product product)	throws ProductRegistryException;
	
}
