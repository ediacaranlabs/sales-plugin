package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.community.ediacaran.sales.entity.ProductImage;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductImageEntityAccess {

	void save(ProductImage entity) throws EntityAccessException;
	
	void update(ProductImage entity) throws EntityAccessException;

	void delete(ProductImage entity) throws EntityAccessException;

	ProductImage findById(Serializable id) throws EntityAccessException;
	
	List<ProductImage> getImagesByProduct(Product product) throws EntityAccessException;
	
	void flush();
	
}
