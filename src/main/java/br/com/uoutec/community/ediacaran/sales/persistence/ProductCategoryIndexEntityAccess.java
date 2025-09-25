package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.community.ediacaran.sales.entity.ProductCategorySearch;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductCategoryIndexEntityAccess {

	void save(ProductCategory entity) throws EntityAccessException;
	
	void update(ProductCategory entity) throws EntityAccessException;

	void delete(ProductCategory entity) throws EntityAccessException;

	ProductCategory findById(int id) throws EntityAccessException;
	
	List<ProductCategory> searchProduct(ProductCategorySearch value, Integer first, Integer max) throws EntityAccessException;
	
	void flush();
	
}
