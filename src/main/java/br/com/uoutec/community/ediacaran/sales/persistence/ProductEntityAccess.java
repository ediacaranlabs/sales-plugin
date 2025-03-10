package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductSearch;
import br.com.uoutec.community.ediacaran.sales.entity.ProductType;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductEntityAccess {

	void save(Product entity) throws EntityAccessException;
	
	void update(Product entity) throws EntityAccessException;

	void delete(Product entity) throws EntityAccessException;

	void saveIndex(Product value) throws EntityAccessException;

	void updateIndex(Product value) throws EntityAccessException;

	void deleteIndex(Product value) throws EntityAccessException;

	boolean ifIndexExist(Product value) throws EntityAccessException;
	
	Product findById(Serializable id) throws EntityAccessException;
	
	List<Product> getProductByType(ProductType serviceType) throws EntityAccessException;
	
	ProductEntitySearchResult searchProduct(ProductSearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
