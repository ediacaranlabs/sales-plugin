package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;

import br.com.uoutec.community.ediacaran.sales.entity.Product;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductIndexEntityAccess {

	void save(Product entity) throws EntityAccessException;
	
	void update(Product entity) throws EntityAccessException;

	void delete(Product entity) throws EntityAccessException;

	Product findById(Serializable id) throws EntityAccessException;
	
	void flush();
	
}
