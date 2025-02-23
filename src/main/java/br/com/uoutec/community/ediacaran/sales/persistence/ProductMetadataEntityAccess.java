package br.com.uoutec.community.ediacaran.sales.persistence;

import java.io.Serializable;
import java.util.List;

import br.com.uoutec.community.ediacaran.sales.entity.ProductMetadata;
import br.com.uoutec.persistence.EntityAccessException;

public interface ProductMetadataEntityAccess {

	void save(ProductMetadata entity) throws EntityAccessException;
	
	void update(ProductMetadata entity) throws EntityAccessException;

	void delete(ProductMetadata entity) throws EntityAccessException;

	ProductMetadata findById(Serializable id) throws EntityAccessException;
	
	List<ProductMetadata> getAll() throws EntityAccessException;
	
	void flush();
	
}
