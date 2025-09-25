package br.com.uoutec.community.ediacaran.sales.persistence;

import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductCategory;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductCategoryEntityAccess {

	void save(ProductCategory entity) throws EntityAccessException;
	
	void update(ProductCategory entity) throws EntityAccessException;

	void delete(ProductCategory entity) throws EntityAccessException;

	ProductCategory findById(int id) throws EntityAccessException;
	
	List<ProductCategory> getProductByParent(ProductCategory parent) throws EntityAccessException;
	
	void flush();
	
}
